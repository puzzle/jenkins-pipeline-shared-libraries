package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class OwaspDependencyCheckSpec extends JenkinsPipelineSpecification {

    def owaspCheck = null

    def setup() {
        owaspCheck = loadPipelineScriptForTest('vars/owaspDependencyCheck.groovy')
    }

    def 'it calls the dependency check' () {
        setup:
            explicitlyMockPipelineStep('dependencyCheckPublisher')
        when:
            owaspCheck.call('app', 'api', tool: 'owasp-dependency-check-5.2.4', extraArgs: '--enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude')
        then:
            1 * getPipelineMock('tool').call(_) >> '/owasp-dependency-check-5.2.4'
            1 * getPipelineMock('withEnv').call(_) >> { _arguments ->
                def envArgs = ['PATH+DC=/owasp-dependency-check-5.2.4/bin']
                assert envArgs == _arguments[0][0]
            }
            1 * getPipelineMock('sh').call('mkdir -p data report')
            1 * getPipelineMock('sh').call('dependency-check.sh --scan \'app\' --scan \'api\' --format \'ALL\' --out report --enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude')
    }
}
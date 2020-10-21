package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class OwaspDependencyCheckSpec extends JenkinsPipelineSpecification {

    def owaspDependencyCheck = null

    def setup() {
        owaspDependencyCheck = loadPipelineScriptForTest('vars/owaspDependencyCheck.groovy')
        explicitlyMockPipelineStep('ansiColor')
        explicitlyMockPipelineStep('dependencyCheckPublisher')
    }

    def 'it should calls the dependency-check tool'() {

        when:
        owaspDependencyCheck.call(
                scan: ['app', 'api'],
                project: 'My Project',
                failOnCVSS: 5,
                enableExperimental: true,
                suppression: ['dependency-check-suppression.xml'],
                tool: 'owasp-dependency-check-5.2.5')

        then:
        1 * getPipelineMock('tool').call('owasp-dependency-check-5.2.5') >> '/path/to/dependency-check'
        1 * getPipelineMock('withEnv').call(_) >> { _arguments ->
            assert ['PATH+DC_HOME=/path/to/dependency-check/bin'] == _arguments[0][0]
        }
        1 * getPipelineMock('sh').call({ it['script'].endsWith('mkdir -p data report') })
        1 * getPipelineMock('sh').call({ it['script'].endsWith('dependency-check.sh --scan \'app\' --scan \'api\' --format \'ALL\' --out \'report\' --suppression \'dependency-check-suppression.xml\' --enableExperimental --failOnCVSS 5 --project \'My Project\'') })

    }

    def 'it should be backward compatible'() {

        when:
        owaspDependencyCheck.call('app', 'api', tool: 'owasp-dependency-check-5.2.4', extraArgs: '--enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude')

        then:
        1 * getPipelineMock('tool').call('owasp-dependency-check-5.2.4') >> '/path/to/dependency-check'
        1 * getPipelineMock('withEnv').call(_) >> { _arguments ->
            assert ['PATH+DC_HOME=/path/to/dependency-check/bin'] == _arguments[0][0]
        }
        1 * getPipelineMock('sh').call({ it['script'].endsWith('mkdir -p data report') })
        1 * getPipelineMock('sh').call({ it['script'].endsWith('dependency-check.sh --scan \'app\' --scan \'api\' --format \'ALL\' --out \'report\' --enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude') })

    }

}
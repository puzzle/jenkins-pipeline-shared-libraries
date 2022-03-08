package groovy.vars

// TODO: Refactor using JUnit
//import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
//
//class OwaspDependencyCheckSpec extends JenkinsPipelineSpecification {
//
//    def owaspDependencyCheck = loadPipelineScriptForTest('vars/owaspDependencyCheck.groovy')
//
//    def setup() {
//        explicitlyMockPipelineStep('ansiColor')
//        explicitlyMockPipelineStep('dependencyCheckPublisher')
//        explicitlyMockPipelineStep('executable')
//    }
//
//    def 'it should call the dependency-check tool'() {
//
//        when:
//        owaspDependencyCheck.call(
//                scan: ['app', 'api'],
//                project: 'My Project',
//                failOnCVSS: 5,
//                enableExperimental: true,
//                suppression: ['dependency-check-suppression.xml'],
//                tool: 'owasp-dependency-check-5.2.5')
//
//        then:
//        1 * getPipelineMock('executable').call(['name':'dependency-check.sh','toolName':'owasp-dependency-check-5.2.5']) >> '/path/bin'
//        1 * getPipelineMock('sh').call({ it['script'].endsWith('mkdir -p data report') })
//        1 * getPipelineMock('sh').call({ it['script'].endsWith('/path/bin/dependency-check.sh --scan \'app\' --scan \'api\' --format \'ALL\' --out \'report\' --suppression \'dependency-check-suppression.xml\' --enableExperimental --failOnCVSS 5 --project \'My Project\'') })
//
//    }
//
//    def 'it should be backward compatible'() {
//
//        when:
//        owaspDependencyCheck.call('app', 'api', tool: 'owasp-dependency-check-5.2.4', extraArgs: '--enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude')
//
//        then:
//        1 * getPipelineMock('executable').call({ it['name'] == 'dependency-check.sh' && it['toolName'] == 'owasp-dependency-check-5.2.4' }) >> '/path/bin'
//        1 * getPipelineMock('sh').call({ it['script'].endsWith('mkdir -p data report') })
//        1 * getPipelineMock('sh').call({ it['script'].endsWith('/path/bin/dependency-check.sh --scan \'app\' --scan \'api\' --format \'ALL\' --out \'report\' --enableExperimental --suppression dependency-check-suppression.xml --failOnCVSS 7 --exclude exclude') })
//
//    }
//
//}
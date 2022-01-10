package groovy.src.com.puzzleitc.jenkins.command.context

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

class JenkinsPipelineContextSpec extends JenkinsPipelineSpecification {

    def jenkinsPipelineContext
    def script

    def setup() {
        script = new Script()
        Map params = [:]
        jenkinsPipelineContext = new JenkinsPipelineContext(script, params)
    }

    def 'It adds the given env variable' () {
        setup:
        def newEnvName = 'TEST'
        def newEnvValue = 'test'
        when:
        jenkinsPipelineContext.setEnv(newEnvName, newEnvValue)
        then:
        script.env[newEnvName] == newEnvValue
    }
}

// Class used to mock Script
class Script {
    def env = [:]
}
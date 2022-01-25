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

    /* TODO:
    def 'It cannot update an env variable defined inside environment {...}' () {
        setup:
        def newEnvName = 'TEST'
        def newEnvValue = 'test'
        script.env[newEnvName] = 'uppps'
        when:
        jenkinsPipelineContext.setEnv(newEnvName, newEnvValue)
        then:
        1 * script.error("""
setEnv of TEST failure!
Variables defined inside 'environment {...}' cannot be overwritten!
Use 'withEnv(...) {...}' instead.""")
        script.env[newEnvName] == newEnvValue
    }
    */

}

// Class used to mock Script
class Script {
    def env = [:]
    def error() {
    }
}
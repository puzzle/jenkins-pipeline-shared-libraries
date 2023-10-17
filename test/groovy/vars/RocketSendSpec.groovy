package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class RocketSendSpec extends JenkinsPipelineSpecification {

    def rocketSend = loadPipelineScriptForTest('vars/rocketSend.groovy')

    def setup() {
        explicitlyMockPipelineStep('executable')
    }

    def 'it should send a RocketChat notification successfully'() {

        setup:
        def webHook = 'https://chat.puzzle.ch/hooks/test'
        def message = 'Test send successful'
        def avatar = 'https://chat.puzzle.ch/emoji-custom/success.png'

        when:
        rocketSend.call(webHook, message, avatar)

        then:
        1 * getPipelineMock('sh').call({ it.contains('curl') }) >> 0
        1 * getPipelineMock('echo').call('RocketChat notification sent successfully')
    }

    def 'it should fail when RocketChat notification fails'() {

        setup:
        def webHook = 'https://chat.puzzle.ch/hooks/bad'
        def message = 'Test send failed'
        def rawMessage = true

        when:
        rocketSend.call(webHook, message, rawMessage)

        then:
        1 * getPipelineMock('sh').call({ it.contains('curl') }) >> 1
        1 * getPipelineMock('error').call('RocketChat notification failed!') >> { throw new RuntimeException() }
        thrown(RuntimeException)
    }
}
import com.puzzleitc.jenkins.command.RocketSendCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def void call(String webHook, String message, String avatar = null, Boolean rawMessage = true) {
    RocketSendCommand command = new RocketSendCommand(new JenkinsPipelineContext(this, [
            webHook: webHook,
            message: message,
            avatar: avatar,
            rawMessage: rawMessage
    ]))
    command.execute()
}
import com.puzzleitc.jenkins.command.RocketSendCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def void call(Map args = [:]) {
    RocketSendCommand command = new RocketSendCommand(new JenkinsPipelineContext(this, args))
    command.execute()
}
import com.puzzleitc.jenkins.command.OpenshiftProcessCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map args = [:]) {
    OpenshiftProcessCommand command = new OpenshiftProcessCommand(new JenkinsPipelineContext(this, args))
    return command.execute()
}

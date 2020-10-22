import com.puzzleitc.jenkins.command.OpenshiftDiffCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:]) {
    OpenshiftDiffCommand command = new OpenshiftDiffCommand(new JenkinsPipelineContext(this, params))
    command.execute()
}
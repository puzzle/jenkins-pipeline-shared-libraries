import com.puzzleitc.jenkins.command.OpenshiftDiffCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:]) {
    def ctx = new JenkinsPipelineContext(this, params)
    OpenshiftDiffCommand command = new OpenshiftDiffCommand(ctx)
    command.execute()
}
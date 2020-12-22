import com.puzzleitc.jenkins.command.AddDeployLinksCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:]) {
    AddDeployLinksCommand command = new AddDeployLinksCommand(new JenkinsPipelineContext(this, params))
    command.execute()
}

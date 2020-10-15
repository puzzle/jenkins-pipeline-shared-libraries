import com.puzzleitc.jenkins.command.OpenshiftStartBuildCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:]) {
    OpenshiftStartBuildCommand command = new OpenshiftStartBuildCommand(new JenkinsPipelineContext(params))
    command.execute()
}
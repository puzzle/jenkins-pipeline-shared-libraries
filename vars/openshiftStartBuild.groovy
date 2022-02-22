import com.puzzleitc.jenkins.command.OpenshiftStartBuildCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map args = [:]) {
    OpenshiftStartBuildCommand command = new OpenshiftStartBuildCommand(new JenkinsPipelineContext(this, args))
    command.execute()
}
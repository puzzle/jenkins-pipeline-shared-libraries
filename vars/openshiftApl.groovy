import com.puzzleitc.jenkins.command.OpenshiftApplyCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map args = [:]) {
    OpenshiftApplyCommand command = new OpenshiftApplyCommand(new JenkinsPipelineContext())
    command.execute()
}
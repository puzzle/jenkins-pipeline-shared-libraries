import com.puzzleitc.jenkins.command.OpenshiftApplyCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call() {
    OpenshiftApplyCommand command = new OpenshiftApplyCommand(new JenkinsPipelineContext())
    return command.execute()
}
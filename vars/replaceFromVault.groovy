import com.puzzleitc.jenkins.command.ReplaceFromVaultCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(String input) {
    ReplaceFromVaultCommand command = new ReplaceFromVaultCommand(input, new JenkinsPipelineContext())
    return command.execute()
}
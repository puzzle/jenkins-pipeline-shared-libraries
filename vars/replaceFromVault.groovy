import com.puzzleitc.jenkins.command.ReplaceFromVaultCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

import static com.puzzleitc.jenkins.Util.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
    def args = parseArgs(namedArgs, positionalArgs, ['text'])
    ReplaceFromVaultCommand command = new ReplaceFromVaultCommand(new JenkinsPipelineContext(args))
    return command.execute()
}
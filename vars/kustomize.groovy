import com.puzzleitc.jenkins.command.KustomizeCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

import static com.puzzleitc.jenkins.util.Args.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
    def args = parseArgs(namedArgs, positionalArgs, ['path'])
    KustomizeCommand command = new KustomizeCommand(new JenkinsPipelineContext(this, args))
    return command.execute()
}
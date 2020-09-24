import com.puzzleitc.jenkins.command.KustomizeCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

import static com.puzzleitc.jenkins.Util.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
    def args = parseArgs(namedArgs, positionalArgs, ['resource'])
    KustomizeCommand command = new KustomizeCommand(new JenkinsPipelineContext(args))
    return command.execute()
}
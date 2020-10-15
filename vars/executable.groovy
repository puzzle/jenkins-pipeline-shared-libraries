import com.puzzleitc.jenkins.command.ExecutableCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext
import static com.puzzleitc.jenkins.Util.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
    def args = parseArgs(namedArgs, positionalArgs, ["name"], ["toolName": null])
    ExecutableCommand command = new ExecutableCommand(new JenkinsPipelineContext(args))
    command.execute()
}

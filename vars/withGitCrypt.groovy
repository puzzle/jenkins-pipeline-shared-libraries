import com.puzzleitc.jenkins.command.GitCryptCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext
import static com.puzzleitc.jenkins.Util.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
    def args = parseArgs(namedArgs, positionalArgs, ['credentialsId', 'body'])
    GitCryptCommand command = new GitCryptCommand(new JenkinsPipelineContext(this, args))
    command.execute()
}

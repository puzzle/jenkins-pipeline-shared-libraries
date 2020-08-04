import com.puzzleitc.jenkins.command.GitCryptCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:], body) {

    GitCryptCommand command = new GitCryptCommand(new JenkinsPipelineContext(params))
    try {
        command.execute()
        body()
    } finally {
        command.cleanUp()
    }
}

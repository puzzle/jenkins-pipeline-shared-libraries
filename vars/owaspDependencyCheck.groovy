import com.puzzleitc.jenkins.command.OwaspDependencyCheckCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:], String... scanDirs) {
    // for backward compatibility: add the unnamed arguments as scanDirs to the parameters
    params.get('scanDirs', []) << scanDirs
    OwaspDependencyCheckCommand command = new OwaspDependencyCheckCommand(new JenkinsPipelineContext(params))
    command.execute()
}

// vim: ft=groovy
// code: language=declarative

import com.puzzleitc.jenkins.command.KubeVersionCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

import static com.puzzleitc.jenkins.Util.parseArgs

def call(Map namedArgs = [:], Object... positionalArgs) {
  def args = parseArgs(namedArgs, positionalArgs, [], ['server': null])
  KubeVersionCommand command = new KubeVersionCommand(new JenkinsPipelineContext(this, args))
  return command.execute()
}
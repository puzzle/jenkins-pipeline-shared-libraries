// vim: ft=groovy
// code: language=declarative

import com.puzzleitc.jenkins.command.KubeVersionCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map args = [:]) {
  KubeVersionCommand command = new KubeVersionCommand(new JenkinsPipelineContext(this, args))
  return command.execute()
}
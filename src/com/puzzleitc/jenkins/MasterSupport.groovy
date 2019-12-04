package com.puzzleitc.jenkins

def sh(String command) {
  def process = new ProcessBuilder(["sh", "-c", command])
                                    .redirectErrorStream(true)
                                    .start()
  // process.inputStream.each_line {println it}
  print process.inputStream.getText()
  process.waitFor()
  return process.exitValue()
}

def copyArtifact(String job, String buildNumber, String artifact, String target) {
   artifactDir = "${env.JENKINS_HOME}/jobs/${job.replaceAll('/', '/jobs/')}/builds/${buildNumber}/archive/"
   sh "cp -av ${artifactDir}/${artifact} ${target}"
}

def run(String scriptFile) {
  GroovyShell shell = new GroovyShell()
  def script = shell.parse(new File(scriptFile))
  def stringWriter = new StringWriter()
  script.out = new PrintWriter(stringWriter)
  script.run()
  println stringWriter.toString()
}

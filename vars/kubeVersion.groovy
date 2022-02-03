// vim: ft=groovy
// code: language=declarative

def call(String server) {
  server = (server != null) ? server.replace('insecure://', 'https://') : 'https://kubernetes.default.svc'

  def out = sh script: "curl -sSk ${server}/version", returnStdout: true
  def version = readJSON text: out

  echo "${server} version: ${version.major}.${version.minor}"

  return "${version.major}.${version.minor}"
}

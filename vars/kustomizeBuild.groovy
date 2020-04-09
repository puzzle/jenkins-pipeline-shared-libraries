import com.puzzleitc.jenkins.util.JenkinsInvoker

def call(String resource) {
    JenkinsInvoker invoker = new JenkinsInvoker()
    invoker.info("start kustomize build")
    invoker.info("resource: $resource")
    withEnv(["PATH+KUSTOMIZE_HOME=${tool 'kustomize'}/bin"]) {
        return sh("kustomize build ${resource}")
    }
}
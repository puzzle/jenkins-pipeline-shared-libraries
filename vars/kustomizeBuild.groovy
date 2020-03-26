def call(String resourceFile) {
    withEnv(["PATH+KUSTOMIZE_HOME=${tool 'kustomize'}/bin"]) {
        sh 'kustomize build ${resourceFile}'
    }
}
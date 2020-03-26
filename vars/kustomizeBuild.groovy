def call(String resourceFile) {
    echo "-- start kustomize build --"
    echo "resource file: $resourceFile"
    withEnv(["PATH+KUSTOMIZE_HOME=${tool 'kustomize'}/bin"]) {
        sh "kustomize build ${resourceFile}"
    }
}
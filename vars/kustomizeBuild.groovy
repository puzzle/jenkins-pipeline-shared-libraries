def call(String resource) {
    echo "-- start kustomize build --"
    echo "resource: $resource"
    withEnv(["PATH+KUSTOMIZE_HOME=${tool 'kustomize'}/bin"]) {
        sh "kustomize build ${resource}"
    }
}
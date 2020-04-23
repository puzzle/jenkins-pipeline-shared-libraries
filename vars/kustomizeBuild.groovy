def call(String resource) {
    echo "-- start kustomize build --"
    echo "resource: $resource"
    withEnv(["PATH+KUSTOMIZE_HOME=${tool 'kustomize'}/bin"]) {
        stdout = sh(script: "kustomize build ${resource}", returnStdout: true)
        echo "Output: ${stdout}";
    }
}
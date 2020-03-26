def call(Map args = [:]) {
    sh 'kustomize build'
}
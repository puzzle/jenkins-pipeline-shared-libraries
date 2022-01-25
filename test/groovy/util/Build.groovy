package groovy.util

// Interface used to mock openshift Build
interface Build {
    def logs(String[] params)
    def object()
}
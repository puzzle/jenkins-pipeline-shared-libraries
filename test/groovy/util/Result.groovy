package groovy.util

// Class used to mock openshift Result
class Result {
    def actions = []
    int status

    def Result() {}

    def int status() {
        return status
    }
}
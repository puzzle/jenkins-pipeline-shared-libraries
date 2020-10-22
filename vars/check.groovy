// Deprecated: Will be implemented as a step.
def mandatoryParameter(parameterName) {
    if (!params.containsKey(parameterName)) {
        currentBuild.result = 'ABORTED'
        error('missing parameter: ' + parameterName)
    }
}

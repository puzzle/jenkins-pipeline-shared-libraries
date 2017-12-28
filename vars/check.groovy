def mandatoryParameter(parameterName) {
    if (!params.containsKey(parameterName)) {
        currentBuild.result = 'ABORTED'
        error('missing parameter: ' + parameterName)
    }
}

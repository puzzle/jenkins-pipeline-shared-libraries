package com.puzzleitc.jenkins.command.context

class JenkinsPipelineContext implements PipelineContext {

    private final JenkinsInvoker invoker = new JenkinsInvoker()
    private final StepParams stepParams

    JenkinsPipelineContext(Map params = [:]) {
        this.stepParams = new StepParams(params, this)
    }

    @Override
    StepParams getStepParams() {
        return stepParams
    }

    @Override
    Object sh(Map map) {
        invoker.callSh(map)
    }

    @Override
    Object withEnv(List<String> env, Closure<Object> closure) {
        invoker.callWithEnv(env, closure)
    }

    @Override
    String tool(String toolName) {
        invoker.callTool(toolName)
    }

    @Override
    void echo(String message) {
        invoker.callEcho(message)
    }

    @Override
    void info(String message) {
        invoker.callAnsiColor("xterm") {
            invoker.callEcho("\033[0;34m${message}\033[0m")
        }
    }

    @Override
    void warn(String message) {
        invoker.callAnsiColor("xterm") {
            invoker.callEcho("\033[0;33m${message}\033[0m")
        }
    }

    @Override
    void fail(String message) {
        invoker.callAnsiColor('xterm') {
            invoker.callEcho("\033[0;31m${message}\033[0m")
        }
        invoker.callError("Build failed")
    }

    @Override
    Object getOpenshift() {
        return invoker.openshiftVar
    }

    @Override
    String lookupEnvironmentVariable(String name) {
        invoker.lookupEnvironmentVariable(name)
    }

    @Override
    String lookupValueFromVault(String path, String key) {
        invoker.lookupValueFromVault(path, key)
    }

    @Override
    String lookupTokenFromCredentials(String credentialsId) {
        return invoker.lookupTokenFromCredentials(credentialsId)
    }

    @Override
    void doWithTemporaryFile(String filePrefix, String fileSuffix, Closure body) {
        File tempFile
        try {
            // TODO: Put temp file in workspace
            tempFile = File.createTempFile(filePrefix, fileSuffix)
            body.call(tempFile)
        } finally {
            if (tempFile) {
                tempFile.delete()
            }
        }
    }

}

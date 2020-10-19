package com.puzzleitc.jenkins.command.context

class JenkinsPipelineContext implements PipelineContext {

    private static final DEFAULT_CREDENTIAL_ID_SUFFIX = '-cicd-deployer'

    private final JenkinsInvoker invoker = new JenkinsInvoker()
    private final StepMetrics stepMetrics = new StepMetrics()
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
    Object withCredentials(List<Object> credentials, Closure<Object> closure) {
        invoker.callWithCredentials(credentials, closure)
    }

    @Override
    Object file(Map map) {
        invoker.callFile(map)
    }

    @Override
    String tool(String toolName) {
        invoker.callTool(toolName)
    }

    @Override
    String executable(String name, String toolName) {
        invoker.callExecutable(name, toolName)
    }

    @Override
    String executable(String name) {
        invoker.callExecutable(name, null)
    }

    @Override
    void echo(String message) {
        invoker.callEcho(message)
    }

    @Override
    void info(String message) {
        invoker.callAnsiColor('xterm') {
            invoker.callEcho("\033[0;34m${message}\033[0m")
        }
    }

    @Override
    void warn(String message) {
        invoker.callAnsiColor('xterm') {
            invoker.callEcho("\033[0;33m${message}\033[0m")
        }
    }

    @Override
    void fail(String message) {
        invoker.callAnsiColor('xterm') {
            invoker.callEcho("\033[0;31m${message}\033[0m")
        }
        invoker.callError('Build failed')
    }

    @Override
    Object getOpenshift() {
        return invoker.openshiftVar
    }

    @Override
    String lookupEnvironmentVariable(String name) {
        invoker.getEnv(name)
    }

    @Override
    String getEnv(String name) {
        invoker.getEnv(name)
    }

    @Override
    void setEnv(String name, String value) {
        invoker.setEnv(name, value)
    }

    @Override
    String lookupValueFromVault(String path, String key) {
        invoker.lookupValueFromVault(path, key)
    }

    @Override
    String lookupServiceAccountToken(String credentialsId, project) {
        if (credentialsId == null) {
            // Token is only needed when not running on Kubernetes cluster
            if (invoker.getEnv('KUBERNETES_PORT') == null) {
                invoker.lookupTokenFromCredentials("${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}")
            } else {
                return null
            }
        } else {
            invoker.lookupTokenFromCredentials(credentialsId)
        }
    }

    @Override
    void doWithTemporaryFile(String content, String fileSuffix, String encoding, Closure body) {
        invoker.callDir("${System.currentTimeMillis()}") {
            try {
                def fileName = "temp${fileSuffix}"
                def absolutePath = "${invoker.callPwd()}/${fileName}"
                invoker.callWriteFile(fileName, content, encoding)
                invoker.callEcho("wrote temporary file: ${absolutePath}")
                body.call(absolutePath)
            } finally {
                invoker.callDeleteDir()
            }
        }
    }

    @Override
    void incrementStepCounter(String stepName) {
        stepMetrics.increment()
    }

}

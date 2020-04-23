package com.puzzleitc.jenkins.command.context

class JenkinsPipelineContext implements PipelineContext {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

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
    String vault(String path, String key) {
        invoker.callVault(path, key)
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

}

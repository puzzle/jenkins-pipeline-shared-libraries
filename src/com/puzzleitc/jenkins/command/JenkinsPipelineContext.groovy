package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.util.JenkinsInvoker

class JenkinsPipelineContext implements PipelineContext {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

    @Override
    Object sh(Object map) {
        return invoker.callSh(map)
    }

    @Override
    Object withEnv(List<String> env, Closure<Object> closure) {
        return invoker.callWithEnv(env, closure)
    }

    @Override
    String tool(String toolName) {
        return invoker.callTool(toolName)
    }

    @Override
    def environment() {
        return invoker.callEnv()
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

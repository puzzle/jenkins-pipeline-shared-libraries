package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.util.JenkinsInvoker

class JenkinsPipelineContext implements PipelineContext {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

    @Override
    Object shell(Object map) {
        return invoker.callSh(map)
    }

    @Override
    Object withEnvironment(List<String> env, Closure<Object> closure) {
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
    void log(String message) {
        invoker.callEcho(message)
    }

    @Override
    void info(String message) {
        invoker.callEcho(message)
    }

    @Override
    void warn(String message) {
        invoker.callEcho(message)
    }

}

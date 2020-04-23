package com.puzzleitc.jenkins.command.context

interface PipelineContext {

    Object sh(Map map)

    Object withEnv(List<String> env, Closure<Object> closure)

    Object withVault(Map map, Closure<Object> closure)

    String tool(String toolName)

    def environment()

    void echo(String message)

    void info(String message)

    void warn(String message)

}
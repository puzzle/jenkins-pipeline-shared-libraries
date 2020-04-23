package com.puzzleitc.jenkins.command.context

interface PipelineContext {

    Object sh(Map map)

    Object withEnv(List<String> env, Closure<Object> closure)

    String tool(String toolName)

    String vault(String path, String key)

    Object getOpenshift()

    void echo(String message)

    void info(String message)

    void warn(String message)

}
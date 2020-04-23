package com.puzzleitc.jenkins.command

interface PipelineContext {

    Object shell(map)

    Object withEnvironment(List<String> env, Closure<Object> closure)

    String tool(String toolName)

    def environment()

    void log(String message)

    void info(String message)

    void warn(String message)

}
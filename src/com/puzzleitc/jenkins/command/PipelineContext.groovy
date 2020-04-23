package com.puzzleitc.jenkins.command

interface PipelineContext {

    Object sh(map)

    Object withEnv(List<String> env, Closure<Object> closure)

    String tool(String toolName)

    def env()

    void log(String message)

    void info(String message)

    void warn(String message)

}
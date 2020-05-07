package com.puzzleitc.jenkins.command.context

interface PipelineContext {

    StepParams getStepParams()

    Object sh(Map map)

    Object withEnv(List<String> env, Closure<Object> closure)

    String tool(String toolName)

    Object getOpenshift()

    void echo(String message)

    void info(String message)

    void warn(String message)

    void fail(String message)

    String lookupValueFromVault(String path, String key)

    String lookupTokenFromCredentials(String credentialsId)

}
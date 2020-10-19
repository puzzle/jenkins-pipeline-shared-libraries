package com.puzzleitc.jenkins.command.context

interface PipelineContext {

    StepParams getStepParams()

    Object sh(Map map)

    Object withEnv(List<String> env, Closure<Object> closure)

    Object withCredentials(List<Object> credentials, Closure<Object> closure)

    Object file(Map map)

    String tool(String toolName)

    String executable(String name, String toolName)
    String executable(String name)

    Object getOpenshift()

    void echo(String message)

    void info(String message)

    void warn(String message)

    void fail(String message)

    String lookupEnvironmentVariable(String name)

    String getEnv(String name)

    void setEnv(String name, String value)

    String lookupValueFromVault(String path, String key)

    String lookupServiceAccountToken(String credentialsId, project)

    void doWithTemporaryFile(String content, String fileSuffix, String encoding, Closure body)

    void incrementStepCounter(String stepName)

}

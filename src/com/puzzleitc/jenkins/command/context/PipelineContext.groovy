package com.puzzleitc.jenkins.command.context

interface PipelineContext {

    StepParams getStepParams()

    Object sh(Map map)

    String getEnv(String name)

    void setEnv(String name, String value)

    Object withEnv(List<String> env, Closure<Object> closure)

    Object withCredentials(List<Object> credentials, Closure<Object> closure)

    Object file(Map map)

    void dir(String path, Closure closure)

    void deleteDir()

    String writeFile(String file, String text, String encoding)

    String pwd()

    String tool(String toolName)

    void withOc(Closure body)

    String executable(String name, String toolName)

    String executable(String name)

    Object getOpenshift()

    void echo(String message)

    void info(String message)

    void warn(String message)

    void fail(String message)

    void dependencyCheckPublisher(Map args)

    void addHtmlBadge(Map args)

    String lookupValueFromVault(String path, String key)

    String lookupServiceAccountToken(String credentialsId, project)

    void doWithTemporaryFile(String content, String fileSuffix, String encoding, Closure body)

}

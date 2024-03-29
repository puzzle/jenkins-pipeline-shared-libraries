package com.puzzleitc.jenkins.command.context

import groovy.json.JsonSlurper

import static com.puzzleitc.jenkins.command.Constants.DEFAULT_OC_TOOL_NAME
import org.jenkinsci.plugins.credentialsbinding.impl.CredentialNotFoundException

class JenkinsPipelineContext implements PipelineContext {

    private static final DEFAULT_CREDENTIAL_ID_SUFFIX = '-cicd-deployer'

    private final Object script
    private final StepParams stepParams

    JenkinsPipelineContext(Object script, Map params = [:]) {
        this.stepParams = new StepParams(params, this)
        this.script = script
    }

    @Override
    StepParams getStepParams() {
        return stepParams
    }

    @Override
    Object sh(Map args) {
        if (args['script'] && !args['script'].toString().startsWith('#!')) {
            // suppress stdout of shell command by passing custom shebang line
            args['script'] = '#!/bin/sh -e\n' + args['script']
        }
        script.sh(args)
    }

    @Override
    String getEnv(String name) {
        return script.env[name]
    }

    @Override
    void setEnv(String name, String value) {
        script.env[name] = value

        if (script.env[name] != value) {
            script.error("""
setEnv of ${name} failure!
Variables defined inside 'environment {...}' cannot be overwritten!
Use 'withEnv(...) {...}' instead.""")
        }
    }

    @Override
    Object withEnv(List<String> env, Closure<Object> closure) {
        script.withEnv(env) {
            closure.call()
        }
    }

    @Override
    Object withCredentials(List<Object> credentials, Closure<Object> closure) {
        script.withCredentials(credentials) {
            closure.call()
        }
    }

    @Override
    Object file(Map args) {
        script.file(args)
    }

    @Override
    void dir(String path, Closure closure) {
        script.dir(path) {
            closure.call()
        }
    }

    @Override
    Object string(Map args) {
        script.string(args)
    }

    @Override
    void deleteDir() {
        script.deleteDir()
    }

    @Override
    String writeFile(String file, String text, String encoding) {
        script.writeFile(file: file, text: text, encoding: encoding)
    }

    @Override
    String pwd() {
        script.pwd()
    }

    @Override
    String tool(String toolName) {
        script.tool(toolName)
    }

    @Override
    void withOc(Closure body) {
        if (this.needsOcInstallation()) {
            def oc_home = this.defaultOcInstallation()
            this.withEnv(["PATH+OC=${oc_home}"]) {
                body()
            }
        } else {
            body()
        }
    }

    private Boolean needsOcInstallation() {
        int status = sh(script: 'command -v oc', returnStatus: true)
        if (status == 0) {
            return false
        }
        return true
    }

    private String defaultOcInstallation() {
        return executable('oc', DEFAULT_OC_TOOL_NAME)
    }

    @Override
    String executable(String name, String toolName) {
        script.executable(name: name, toolName: toolName)
    }

    @Override
    String executable(String name) {
        script.executable(name: name)
    }

    @Override
    void echo(String message) {
        script.echo(message)
    }

    @Override
    void info(String message) {
        script.ansiColor('xterm') {
            script.echo("\033[0;34m${message}\033[0m")
        }
    }

    @Override
    void warn(String message) {
        script.ansiColor('xterm') {
            script.echo("\033[0;33m${message}\033[0m")
        }
    }

    @Override
    void fail(String message) {
        script.ansiColor('xterm') {
            script.echo("\033[0;31m${message}\033[0m")
        }
        script.error('Build failed')
    }

    @Override
    Object getOpenshift() {
        return script.openshift
    }

    @Override
    void dependencyCheckPublisher(Map args) {
        script.dependencyCheckPublisher(args)
    }

    @Override
    void addHtmlBadge(Map args) {
        script.addHtmlBadge(args)
    }

    @Override
    String lookupValueFromVault(String path, String key) {
        script.withVault(vaultSecrets: [[path: path, engineVersion: 2, secretValues: [[envVar: 'secretValue', vaultKey: key]]]]) {
            return script.secretValue
        }
    }

    @Override
    String lookupServiceAccountToken(String credentialsId, project) {
        if (credentialsId == null) {
            try {
                lookupTokenFromCredentials("${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}")
            } catch (CredentialNotFoundException e) {
                // if jenkins runs in K8S use the default token
                if (getEnv('KUBERNETES_PORT') == null) {
                    throw e
                } else {
                    return null
                }
            }
        } else {
            lookupTokenFromCredentials(credentialsId)
        }
    }

    private String lookupTokenFromCredentials(String credentialsId) {
        withCredentials([script.string(credentialsId: credentialsId, variable: 'secretValue')]) {
            try {
                def jsonObj = new JsonSlurper().parseText(script.secretValue)
                return new String(jsonObj.token.decodeBase64())
            } catch (Exception e) {
                // it's not a json. maybe its a plain token?
                return script.secretValue
            }
        }
    }

    @Override
    void doWithTemporaryFile(String content, String fileSuffix, String encoding, Closure body) {
        dir("${System.currentTimeMillis()}") {
            try {
                def fileName = "temp${fileSuffix}"
                def absolutePath = "${pwd()}/${fileName}"
                writeFile(fileName, content, encoding)
                echo("wrote temporary file: ${absolutePath}")
                body.call(absolutePath)
            } finally {
                deleteDir()
            }
        }
    }
}

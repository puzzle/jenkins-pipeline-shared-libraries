package com.puzzleitc.jenkins.command.context

import groovy.json.JsonSlurper

/** this is deliberately not a real class, otherwise the build-in methods of Jenkins do not work */

Object getOpenshiftVar() {
    return openshift
}

Map getEnvVar() {
    return env
}

Object callSh(Map args) {
    if (args['script'] && !args['script'].toString().startsWith('#!')) {
        // suppress stdout of shell command by passing custom shebang line
        args['script'] = '#!/bin/sh -e\n' + args['script']
    }
    sh(args)
}

Object callWithEnv(List<String> env, Closure<Object> closure) {
    withEnv(env) {
        closure.call()
    }
}

Object callWithCredentials(List<Object> credentials, Closure<Object> closure) {
    withCredentials(credentials) {
        closure.call()
    }
}

Object callFile(Map args) {
    file(args)
}

def callAnsiColor(String colorMapName, Closure<Void> closure) {
    ansiColor(colorMapName) {
        closure.call()
    }
}

def callDir(String path, Closure closure) {
    dir(path) {
        closure.call()
    }
}

def callDeleteDir() {
    deleteDir()
}

String callWriteFile(String file, String text, String encoding) {
    writeFile(file: file, text: text, encoding: encoding)
}

String callPwd() {
    pwd()
}

String callTool(String toolName) {
    tool(toolName)
}

def callEcho(String message) {
    echo message
}

def callError(String message) {
    error(message)
}

def callExit(int status) {
    exit(status)
}

String lookupEnvironmentVariable(String name) {
    return env[name]
}

String lookupValueFromVault(String path, String key) {
    withVault(vaultSecrets: [[path: path, engineVersion: 2, secretValues: [[envVar: 'secretValue', vaultKey: key]]]]) {
        return secretValue
    }
}

String lookupTokenFromCredentials(String credentialsId) {
    withCredentials([string(credentialsId: credentialsId, variable: 'secretValue')]) {
        try {
            def jsonObj = new JsonSlurper().parseText(secretValue)
            return new String(jsonObj.token.decodeBase64())
        } catch (Exception e) {
            // it's not a json. maybe its a plain token?
            return secretValue
        }
    }
}

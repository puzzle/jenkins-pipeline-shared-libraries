package com.puzzleitc.jenkins.util

/** this is deliberately not a real class, otherwise the build-in methods of Jenkins do not work */

Object callSh(map) {
    return sh(map)
}

Object callWithEnv(List<String> env, Closure<Object> closure) {
    withEnv(env) {
        return closure.call()
    }
}

String toolHome(String toolName) {
    return tool(toolName)
}

def getEnvironment() {
    return env
}

void info(String message) {
    ansiColor('xterm') {
        echo "\033[0;34m${message}\033[0m"
    }
}

void warn(String message) {
    ansiColor('xterm') {
        echo "\033[0;33m${message}\033[0m"
    }
}
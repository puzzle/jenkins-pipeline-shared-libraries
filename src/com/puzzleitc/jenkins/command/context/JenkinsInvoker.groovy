package com.puzzleitc.jenkins.command.context

/** this is deliberately not a real class, otherwise the build-in methods of Jenkins do not work */

Object callSh(Map map) {
    sh(map)
}

Object callWithEnv(List<String> env, Closure<Object> closure) {
    withEnv(env) {
        closure.call()
    }
}

Object callWithVault(Map map, Closure<Object> closure) {
    withVault(map) {
        closure.call()
    }
}

def callAnsiColor(String colorMapName, Closure<Void> closure) {
    ansiColor(colorMapName) {
        closure.call()
    }
}

String callTool(String toolName) {
    tool(toolName)
}

String callEnv() {
    return env
}

def callEcho(String message) {
    echo message
}
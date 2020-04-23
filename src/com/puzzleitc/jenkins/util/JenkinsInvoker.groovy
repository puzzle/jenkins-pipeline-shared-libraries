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

String callTool(String toolName) {
    return tool(toolName)
}

def callEnv() {
    return env
}

void callEcho(String message) {
        echo message
}

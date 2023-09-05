package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

import java.util.regex.Pattern

class ExecutableCommand {

    private final PipelineContext ctx

    ExecutableCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Node getCurrentNode() {
        String nodeName = ctx.getEnv('NODE_NAME')
        if (nodeName.equals('master')) {
            return Jenkins.get()
        } else {
            return Jenkins.get().getNode(nodeName)
        }
    }

    // Search executable in PATH of current Jenkins node
    String searchInPath(String executable) {
        Node node = getCurrentNode()
        for (def path : ctx.getEnv('PATH').split(Pattern.quote(File.pathSeparator))) {
            ctx.info(path)
            if (node.createPath(path).child(executable).exists()) {
                return path
            }
        }
        return null
    }

    Object execute() {
        String executable = ctx.stepParams.getRequired('name')
        String toolName = ctx.stepParams.getOptional('toolName')

        // Was executable found/installed in earlier invocation?
        String exePath = ctx.getEnv("executable_${executable}_path")
        if (exePath) {
            return exePath
        }

        // Is executable in PATH of current Jenkins node?
        exePath = searchInPath(executable)

        // Executable not found, install via tool
        if (!exePath) {
            def toolHome = ctx.tool(toolName ? toolName : executable)
            Node node = getCurrentNode()
            if (node.createPath(toolHome).child('bin').child(executable).exists()) {
                exePath = "${toolHome}/bin"
            } else {
                exePath = toolHome
            }
        }

        // Store path for later invocations
        ctx.setEnv("executable_${executable}_path", exePath)

        return exePath
    }
}

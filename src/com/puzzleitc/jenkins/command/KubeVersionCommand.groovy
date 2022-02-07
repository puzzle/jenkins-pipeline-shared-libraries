package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class KubeVersionCommand {

    private final PipelineContext ctx

    KubeVersionCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- kubeVersion --')
        def server = ctx.stepParams.getOptional('server') as String

        server = (server != null) ? server.replace('insecure://', 'https://') : 'https://kubernetes.default.svc'

        def out = ctx.sh(script: "curl -sSk ${server}/version", returnStdout: true)
        def version = readJSON text: out
        ctx.echo("${server} version: ${version.major}.${version.minor}")

        return "${version.major}.${version.minor}"
    }
}

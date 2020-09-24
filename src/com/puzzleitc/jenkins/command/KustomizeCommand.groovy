package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class KustomizeCommand {

    private final PipelineContext ctx

    KustomizeCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- kustomize --')
        def path = ctx.stepParams.getRequired('path')
        def kustomizeHome = ctx.tool('kustomize')
        ctx.echo("resource: $path")
        ctx.withEnv(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            ctx.sh(script: "kustomize build ${path}", returnStdout: true)
        }
    }

}
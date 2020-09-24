package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class KustomizeCommand {

    private final PipelineContext ctx

    KustomizeCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- kustomize --')
        def resource = ctx.stepParams.getRequired('resource')
        def kustomizeHome = ctx.tool('kustomize')
        ctx.echo("resource: $resource")
        ctx.withEnv(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            ctx.sh(script: "kustomize build ${resource}", returnStdout: true)
        }
    }

}
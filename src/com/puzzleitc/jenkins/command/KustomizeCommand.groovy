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
        def kustomizePath = ctx.executable('kustomize')
        ctx.echo("resource: $path")
	ctx.sh(script: "${kustomizePath}/kustomize build ${path}", returnStdout: true)
    }

}

package com.puzzleitc.jenkins.command

class KustomizeCommand {

    private final String resource;
    private final PipelineContext ctx;

    KustomizeCommand(String resource, PipelineContext ctx) {
        this.ctx = ctx
        this.resource = resource
    }

    Object execute() {
        ctx.info("-- start kustomize build --")
        ctx.echo("resource: $resource")
        def kustomizeHome = ctx.tool("kustomize")
        ctx.withEnv(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            return ctx.sh(script: "kustomize build ${resource}", returnStdout: true)
        }
    }

}
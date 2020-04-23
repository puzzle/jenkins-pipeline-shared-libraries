package com.puzzleitc.jenkins.command

class KustomizeCommand {

    private final String resource;
    private final String kustomizeHome;
    private final PipelineContext ctx;

    KustomizeCommand(String resource, PipelineContext ctx) {
        this.ctx = ctx
        this.resource = resource
        this.kustomizeHome = ctx.tool("kustomize")
    }

    Object execute() {
        ctx.log("-- start kustomize build --")
        ctx.log("resource: $resource")
        ctx.withEnv(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            return ctx.sh(script: "kustomize build ${resource}", returnStdout: true)
        }
    }

}
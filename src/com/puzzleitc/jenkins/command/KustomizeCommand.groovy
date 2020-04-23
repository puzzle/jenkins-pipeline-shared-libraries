package com.puzzleitc.jenkins.command

class KustomizeCommand {

    private final String resource;
    private final PipelineContext ctx;

    KustomizeCommand(String resource, PipelineContext ctx) {
        this.ctx = ctx
        this.resource = resource
    }

    Object execute() {
        ctx.log("-- start kustomize build --")
        ctx.log("resource: $resource")
        def kustomizeHome = ctx.tool("kustomize")
        ctx.withEnvironment(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            return ctx.shell(script: "kustomize build ${resource}", returnStdout: true)
        }
    }

}
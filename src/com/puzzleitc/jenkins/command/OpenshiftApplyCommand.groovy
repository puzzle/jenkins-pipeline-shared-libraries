package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftApplyCommand {

    private final PipelineContext ctx

    OpenshiftApplyCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info("-- openshiftApply --")
        ctx.openshift.withCluster("OpenShiftCloudscaleProduction") {
            ctx.openshift.withCredentials("openshift_cloudscale_jenkins_cicd_prod_token_client_plugin") {
                ctx.openshift.withProject("pitc-wekan-cicd-test-kustomize-2") {
                    ctx.echo("Working on namespace: " + ctx.openshift.project())
                }
            }
        }
    }

}

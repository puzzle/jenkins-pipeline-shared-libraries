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
            def saToken = ctx.lookupTokenFromCredentials("pitc-wekan-cicd-test-kustomize-3-cicd-deployer")
            ctx.echo("Token: ${saToken}")
            ctx.openshift.withProject("pitc-wekan-cicd-test-kustomize-3") {
                ctx.openshift.withCredentials(saToken) {
                    ctx.echo("OpenShift whoami: ${ctx.openshift.raw('whoami')}")
                    ctx.echo("OpenShift cluster: ${ctx.openshift.cluster()}")
                    ctx.echo("OpenShift project: ${ctx.openshift.project()}")

                    /*
                    openshift.raw("convert", "-f", "mongodb.yaml")
                    result = openshift.apply(replaceSecretsFromVault(readFile(file: "mongodb.yaml")), "-l", "app=mongodb", "-n", "$PROJECT_NAME", "--prune")
                    echo "Overall status: ${result.status}"
                    echo "Actions performed: ${result.actions[0].cmd}"
                    echo "Operation output:\n${result.out}"
                    openshift.selector("dc", "mongodb").rollout().status()
                    */
                }
            }
        }
    }

}

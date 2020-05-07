package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftApplyCommand {

    private final PipelineContext ctx

    OpenshiftApplyCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info("-- openshiftApply --")

        def projectName = ctx.stepParams.lookupRequired("projectName")
        ctx.stepParams.lookupRequired("foo")

        def saToken = ctx.lookupTokenFromCredentials("pitc-wekan-cicd-test-kustomize-2-cicd-deployer")
        ctx.openshift.withCluster("OpenShiftCloudscaleProduction") {
            ctx.openshift.withProject("pitc-wekan-cicd-test-kustomize-2") {
                ctx.openshift.withCredentials(saToken) {
                    ctx.echo("OpenShift cluster: ${ctx.openshift.cluster()}")
                    ctx.echo("OpenShift project: ${ctx.openshift.project()}")
                    ctx.echo("OpenShift whoami: ${ctx.openshift.raw('whoami').out.trim()}")

                    /*
                    // openshift.raw("convert", "-f", "mongodb.yaml")
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

package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftApplyCommand {

    private static final DEFAULT_CREDENTIAL_ID_SUFFIX = "-cicd-deployer"

    private final PipelineContext ctx

    OpenshiftApplyCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info("-- openshiftApply --")
        String configuration = ctx.stepParams.lookupRequired("configuration")
        String project = ctx.stepParams.lookupRequired("project")
        String cluster = ctx.stepParams.lookupOptional("cluster", null)
        String credentialId = ctx.stepParams.lookupOptional("credentialId", "${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}")
        String saToken = ctx.lookupTokenFromCredentials(credentialId)
        ctx.openshift.withCluster(cluster) {
            ctx.openshift.withProject(project) {
                ctx.openshift.withCredentials(saToken) {
                    ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                    ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                    ctx.echo("openshift project: ${ctx.openshift.project()}")

                    ctx.echo("todo: apply configuration")
                    ctx.echo(configuration)

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

package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftApplyCommand {

    private static final DEFAULT_CREDENTIAL_ID_SUFFIX = "-cicd-deployer"
    private static final DEFAULT_OC_TOOL_NAME = "oc_3_11"

    private final PipelineContext ctx

    OpenshiftApplyCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    // TODO: Globale Cluster Konfiguration?
    Object execute() {
        ctx.info("-- openshiftApply --")
        def configuration = ctx.stepParams.getRequired("configuration") as String
        def project = ctx.stepParams.getRequired("project")
        def cluster = ctx.stepParams.getOptional("cluster", null)
        def appLabel = ctx.stepParams.getRequired("appLabel") as String
        def rolloutSelector = ctx.stepParams.getOptional("rolloutSelector", [:]) as Map
        def credentialId = ctx.stepParams.getOptional("credentialId", "${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}") as String
        def saToken = ctx.lookupTokenFromCredentials(credentialId)
        def ocHome = ctx.tool(DEFAULT_OC_TOOL_NAME)
        ctx.withEnv(["PATH+OC_HOME=${ocHome}/bin"]) {
            ctx.openshift.withCluster(cluster) {
                ctx.openshift.withProject(project) {
                    ctx.openshift.withCredentials(saToken) {
                        ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                        ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                        ctx.echo("openshift project: ${ctx.openshift.project()}")
                        ocConvert(configuration)
                        ocApply(configuration, appLabel)
                        ocRollout(appLabel, rolloutSelector)
                    }
                }
            }
        }
    }

    private void ocConvert(String configuration) {
        ctx.doWithTemporaryFile("convert", ".markup") {
            File tempFile ->
                tempFile.write(configuration)
                // TODO call oc convert
                // ctx.openshift.raw("convert", "-f", tempFile.absolutePath)
        }
    }

    private void ocApply(String configuration, String appLabel) {
        def result = ctx.openshift.apply(configuration, "-l", "app=${appLabel}", "--prune")
        ctx.echo("openshift result action: ${result.actions[0].cmd}")
        ctx.echo("openshift result status: ${result.status}")
        ctx.echo("openshift result output:\n${result.out}")
    }

    private void ocRollout(String app, Map rolloutSelector) {
        if (rolloutSelector.isEmpty()) {
            ctx.echo("waiting for dc with selector ${app} to be rolled out")
            ctx.openshift.selector("dc", app).rollout().status()
        } else {
            ctx.echo("waiting for dc with selector ${rolloutSelector} to be rolled out")
            ctx.openshift.selector("dc", rolloutSelector).rollout().status()
        }
    }

}

package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftApplyCommand {

    private static final DEFAULT_CREDENTIAL_ID_SUFFIX = "-cicd-deployer"
    private static final DEFAULT_OC_TOOL_NAME = "oc_3_11"

    private final PipelineContext ctx

    OpenshiftApplyCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    // TODO:
    // - OC CLI Version (tool) konfigurierbar?
    // - Direkt oc cli über Shell verwenden?
    // - Ist -n nötig, wenn in withProject Klammer?
    // - Globale Cluster Konfiguration?
    // - Gehören Labels nicht eher ins Template?
    // - Parameter app Optional?
    Object execute() {
        ctx.info("-- openshiftApply --")
        def configuration = ctx.stepParams.getRequired("configuration")
        def project = ctx.stepParams.getRequired("project")
        def cluster = ctx.stepParams.getOptional("cluster", null)
        def app = ctx.stepParams.getOptional("app", null)
        def credentialId = ctx.stepParams.getOptional("credentialId", "${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}") as String
        def saToken = ctx.lookupTokenFromCredentials(credentialId)
        def ocHome = ctx.tool(DEFAULT_OC_TOOL_NAME);
        ctx.withEnv(["PATH+OC_HOME=${ocHome}/bin"]) {
            ctx.openshift.withCluster(cluster) {
                ctx.openshift.withProject(project) {
                    ctx.openshift.withCredentials(saToken) {
                        ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                        ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                        ctx.echo("openshift project: ${ctx.openshift.project()}")

                        /*
                        openshift.raw("convert", "-f", "mongodb.yaml")
                        */
                        def result = ctx.openshift.apply(configuration, "-l", "app=${app}", "-n", project, "--prune")
                        ctx.echo("openshift result status: ${result.status}")
                        ctx.echo("openshift result actions: ${result.actions[0].cmd}")
                        ctx.echo("openshift result output:\n${result.out}")
                        ctx.openshift.selector("dc", app).rollout().status()
                    }
                }
            }
        }
    }

}

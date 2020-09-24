package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftDiffCommand {

    private static final DEFAULT_OPENSHIFT_DIFF_TOOL_NAME = "openshift-diff"

    private final PipelineContext ctx

    OpenshiftDiffCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- openshiftDiff --')
        def configuration = ctx.stepParams.getRequired('configuration') as String
        def project = ctx.stepParams.getRequired('project')
        def cluster = ctx.stepParams.getOptional('cluster', null)
        def openshiftDiffHome = ctx.tool(DEFAULT_OPENSHIFT_DIFF_TOOL_NAME)
        def credentialId = ctx.stepParams.getOptional('credentialId', null) as String
        def saToken = ctx.lookupServiceAccountToken(credentialId, project)
        ctx.withEnv(["PATH+OPENSHIFT_DIFF_HOME=${openshiftDiffHome}/bin"]) {
            ctx.openshift.withCluster(cluster) {
                ctx.openshift.withProject(project) {
                    ctx.openshift.withCredentials(saToken) {
                        ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                        ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                        ctx.echo("openshift project: ${ctx.openshift.project()}")
                        ctx.sh(script: "openshift-diff -n ${project} <<< '${configuration}'")
                    }
                }
            }
        }
    }

}

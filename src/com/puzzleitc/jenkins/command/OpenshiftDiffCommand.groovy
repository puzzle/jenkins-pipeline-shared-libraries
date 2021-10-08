package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftDiffCommand {

    private static final DEFAULT_OPENSHIFT_DIFF_TOOL = 'openshift_diff'

    private final PipelineContext ctx

    OpenshiftDiffCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- openshiftDiff --')
        def configuration = ctx.stepParams.getRequired('configuration') as String
        def project = ctx.stepParams.getRequired('project')
        def cluster = ctx.stepParams.getOptional('cluster')
        def ocPath = ctx.executable('oc')
        def openshiftDiffPath = ctx.executable('openshift-diff', DEFAULT_OPENSHIFT_DIFF_TOOL)
        def insecureSkipTlsVerify = ctx.stepParams.getOptional("insecureSkipTlsVerify")
        def saToken = ctx.stepParams.getOptional('token') as String
        if (!saToken) {
            def credentialsId = ctx.stepParams.getOptional('credentialsId') as String
            saToken = ctx.lookupServiceAccountToken(credentialsId, project)
        }
        ctx.withEnv(["PATH+OPENSHIFT_DIFF=${ocPath}:${openshiftDiffPath}"]) {
            ctx.openshift.withCluster(cluster, saToken) {
                ctx.openshift.withProject(project) {
                    ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                    ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                    ctx.echo("openshift project: ${project}")
                    ctx.sh(script: "openshift-diff --server=${ctx.openshift.cluster()} ${insecureSkipTlsVerify ? '--insecure-skip-tls-verify': '' } ${saToken ? '--token=' + saToken : ''} -n ${project} <<< '${configuration}'")
                }
            }
        }
    }

}

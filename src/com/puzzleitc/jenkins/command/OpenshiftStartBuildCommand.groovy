package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftStartBuildCommand {

    private final PipelineContext ctx

    OpenshiftStartBuildCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- openshiftStartBuild --')
        def project = ctx.stepParams.getRequired('project')
        def cluster = ctx.stepParams.getRequired('cluster')
        def buildConfigName = ctx.stepParams.getRequired('buildConfigName')
        def fromDir = ctx.stepParams.getOptional('fromDir') as String
        def fromFile = ctx.stepParams.getOptional('fromFile') as String
        def credentialsId = ctx.stepParams.getOptional('credentialsId') as String
        def saToken = ctx.lookupServiceAccountToken(credentialsId, project)
        if (ctx.needsOcInstallation()) {
            def oc_home = ctx.defaultOcInstallation()
            ctx.withEnv(["PATH+OC=${oc_home}"]) {
                executeOC(cluster, project, saToken, buildConfigName, fromDir, fromFile)
            }
        } else {
            executeOC(cluster, project, saToken, buildConfigName, fromDir, fromFile)
        }
    }

    private void executeOC(cluster, project, saToken, buildConfigName, fromDir, fromFile) {
        ctx.openshift.withCluster(cluster) {
            ctx.openshift.withProject(project) {
                ctx.openshift.withCredentials(saToken) {
                    ctx.echo("openshift whoami: ${ctx.openshift.raw('whoami').out.trim()}")
                    ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                    ctx.echo("openshift project: ${ctx.openshift.project()}")
                    def bc = ctx.openshift.selector("bc/${buildConfigName}")
                    def build = bc.startBuild(createStartBuildParams(fromDir, fromFile))
                    def result = build.logs('-f')
                    ctx.echo("oc startBuild action: ${result.actions[0].cmd}")
                    ctx.echo("oc startBuild status: ${result.status}")
                    if (build.object().status.phase == 'Failed') {
                        ctx.fail("openshift build ${build.object().metadata.name} failed")
                    }
                }
            }
        }
    }

    private static String[] createStartBuildParams(String fromDir, String fromFile) {
        def result = []
        if (fromDir) {
            result << "--from-dir=${fromDir}"
        }
        if (fromFile) {
            result << "--from-file=${fromFile}"
        }
        return result
    }

}

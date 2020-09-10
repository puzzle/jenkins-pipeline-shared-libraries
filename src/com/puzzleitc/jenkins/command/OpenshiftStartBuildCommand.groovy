package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

import static com.puzzleitc.jenkins.command.Constants.DEFAULT_CREDENTIAL_ID_SUFFIX
import static com.puzzleitc.jenkins.command.Constants.DEFAULT_OC_TOOL_NAME

class OpenshiftStartBuildCommand {

    private final PipelineContext ctx

    OpenshiftStartBuildCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info("-- openshiftStartBuild --")
        def project = ctx.stepParams.getRequired("project")
        def cluster = ctx.stepParams.getRequired("cluster")
        def buildConfigName = ctx.stepParams.getRequired("buildConfigName")
        def fromDir = ctx.stepParams.getOptional("fromDir", null) as String
        def fromFile = ctx.stepParams.getOptional("fromFile", null) as String
        def credentialId = ctx.stepParams.getOptional("credentialId", "${project}${DEFAULT_CREDENTIAL_ID_SUFFIX}") as String
        def saToken = ctx.lookupTokenFromCredentials(credentialId)
        def ocHome = ctx.tool(DEFAULT_OC_TOOL_NAME)
        ctx.withEnv(["PATH+OC_HOME=${ocHome}/bin"]) {
            ctx.openshift.withCluster(cluster) {
                ctx.openshift.withProject(project) {
                    ctx.openshift.withCredentials(saToken) {
                        ctx.echo("openshift cluster: ${ctx.openshift.cluster()}")
                        ctx.echo("openshift project: ${ctx.openshift.project()}")
                        def bc = ctx.openshift.selector("bc/${buildConfigName}")
                        def build = bc.startBuild(createStartBuildParams(fromDir, fromFile))
                        def result = build.logs("-f")
                        ctx.echo("oc startBuild action: ${result.actions[0].cmd}")
                        ctx.echo("oc startBuild status: ${result.status}")
                        if (build.object().status.phase == 'Failed') {
                            ctx.fail("openshift build ${build.object().metadata.name} failed")
                        }
                    }
                }
            }
        }
    }

    private String[] createStartBuildParams(String fromDir, String fromFile) {
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

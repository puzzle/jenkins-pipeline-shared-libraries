package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext
import com.puzzleitc.jenkins.command.context.StepParams

class OwaspDependencyCheckCommand {

    private static final DEFAULT_DC_TOOL_NAME = 'owasp-dependency-check-5.2.4'

    private final PipelineContext ctx

    OwaspDependencyCheckCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- owaspDependencyCheck --')
        def dcTool = ctx.stepParams.getOptional('tool', DEFAULT_DC_TOOL_NAME) as String
        def dcHome = ctx.tool(dcTool)
        ctx.withEnv(["PATH+DC_HOME=${dcHome}/bin"]) {
            ctx.sh(script: 'mkdir -p data report')
            ctx.sh(script: "dependency-check.sh ${createDependencyCheckArgs(ctx.stepParams)}")
        }
        ctx.dependencyCheckPublisher(pattern: 'report/dependency-check-report.xml')
    }

    private String createDependencyCheckArgs(StepParams stepParams) {
        def scanArgs = stepParams.getRequired('scanDirs').collect { "--scan '$it'" }.join(' ')
        def args = "${scanArgs} --format 'ALL' --out report ${stepParams.getOptional('extraArgs', '')}"
        ctx.echo("DependencyCheckArgs: $args")
        return args
    }
}

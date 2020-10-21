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
        def dcArgs = createDependencyCheckArgs(ctx.stepParams)
        ctx.echo("dependency-check tool: $dcTool")
        ctx.echo("dependency-check arguments: $dcArgs")
        ctx.withEnv(["PATH+DC_HOME=${dcHome}/bin"]) {
            ctx.sh(script: 'mkdir -p data report')
            ctx.sh(script: "dependency-check.sh $dcArgs")
        }
        ctx.dependencyCheckPublisher(pattern: 'report/dependency-check-report.xml')
    }

    private String createDependencyCheckArgs(StepParams stepParams) {
        def result = stepParams.getRequired('scanDirs').collect { "--scan '$it'" }.join(' ')
        result << ' --format ALL --out report'
        result << " ${stepParams.getOptional('extraArgs', '')}"
        return result
    }
}

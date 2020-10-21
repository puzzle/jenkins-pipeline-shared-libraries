package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext
import com.puzzleitc.jenkins.command.context.StepParams

class OwaspDependencyCheckCommand {

    private static final DEFAULT_DC_TOOL_NAME = 'owasp-dependency-check-5.2.4'

    private final PipelineContext ctx

    OwaspDependencyCheckCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- owaspDependencyCheck --')
        def dcTool = ctx.stepParams.getOptional('tool', DEFAULT_DC_TOOL_NAME) as String
        def dcPath = ctx.executable(dcTool)
        def dcArgs = createDependencyCheckArgs(ctx.stepParams)
        ctx.echo("tool: ${dcTool}")
        ctx.echo("arguments: ${dcArgs}")
        ctx.sh(script: 'mkdir -p data report')
        ctx.sh(script: "${dcPath}/dependency-check.sh ${dcArgs}")
        ctx.dependencyCheckPublisher(pattern: 'report/dependency-check-report.xml')
    }

    private String createDependencyCheckArgs(StepParams stepParams) {
        StringBuilder result = new StringBuilder()
        result.append(stepParams.getRequired('scan').collect { "--scan '$it'" }.join(' '))
        result.append(" --format 'ALL' --out 'report'")
        if (stepParams.contains('suppression')) {
            def suppression = ensureList(stepParams.getOptional('suppression'))
            result.append(suppression.collect { " --suppression '$it'" }.join(''))
        }
        if (stepParams.contains('exclude')) {
            def exclude = ensureList(stepParams.getOptional('exclude'))
            result.append(exclude.collect { " --exclude '$it'" }.join(''))
        }
        if (stepParams.getOptional('enableExperimental', false)) {
            result.append(' --enableExperimental')
        }
        if (stepParams.contains('failOnCVSS')) {
            result.append(" --failOnCVSS ${stepParams.getOptional('failOnCVSS')}")
        }
        if (stepParams.contains('project')) {
            result.append(" --project '${stepParams.getOptional('project')}'")
        }
        if (stepParams.contains('extraArgs')) {
            result.append(" ${stepParams.getOptional('extraArgs')}")
        }
        return result.toString()
    }

    private List ensureList(Object object) {
        return object instanceof List ? object : [object]
    }
}

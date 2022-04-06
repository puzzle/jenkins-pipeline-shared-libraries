package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class OpenshiftProcessCommand {

    private static final VALID_OUTPUT_FORMAT = ['json', 'yaml']

    private final PipelineContext ctx

    OpenshiftProcessCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- openshiftProcess --')
        ctx.info('-- openshiftProcess start debug --')
        ctx.echo("params: ${ctx.getStepParams()}")
        def envs = ctx.sh(script: 'env', returnStdout: true)
        ctx.echo("env: ${envs}")
        ctx.info('-- openshiftProcess end debug --')
        def result
        def templateFilePath = ctx.stepParams.getRequired('templateFilePath')
        def params = ctx.stepParams.getOptional('params')
        def paramFile = ctx.stepParams.getOptional('paramFile') as String
        def ignoreUnknownParameters = ctx.stepParams.getOptional('ignoreUnknownParameters', false) as boolean
        def labels = ctx.stepParams.getOptional('labels')
        def output = ctx.stepParams.getOptional('output', 'json') as String

        validateOutputFormat(output)
        ctx.echo("template file: ${templateFilePath}")

        def processScript = 'oc process' + ' ' + '-f ' + templateFilePath
        if (params) {
            processScript = processScript + ' ' + params.join(' ')
        }
        if (labels) {
            processScript = processScript + ' -l ' + labels.join(' -l ')
        }
        if (paramFile) {
            processScript = processScript + ' ' + '--param-file=' + paramFile
        }
        processScript = processScript + ' ' +
            '--local=true' + ' ' +
            '--ignore-unknown-parameters=' + ignoreUnknownParameters + ' ' +
            '--output=' + output

        ctx.withOc() {
            result = executeOC(processScript)
        }
        return result
    }

    private Object executeOC(String processScript) {
        return ctx.sh(script: processScript, returnStdout: true)
    }

    private void validateOutputFormat(String validateOutput) {
        if (!VALID_OUTPUT_FORMAT.contains(validateOutput?.toLowerCase())) {
            ctx.fail("Unsupported output format: ${validateOutput}")
        }
    }

}

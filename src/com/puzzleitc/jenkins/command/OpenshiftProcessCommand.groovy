package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

import static com.puzzleitc.jenkins.command.Constants.DEFAULT_OC_TOOL_NAME

class OpenshiftProcessCommand {

    private final PipelineContext ctx

    OpenshiftProcessCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    String execute() {
        ctx.info('-- openshiftProcess --')
        def template = ctx.stepParams.getRequired('template') as String
        def params = ctx.stepParams.getOptional('params')
        def paramFile = ctx.stepParams.getOptional('paramFile') as String
        ctx.echo("template file: ${template}")
        ctx.withEnv(["PATH+OC=${ctx.executable('oc', DEFAULT_OC_TOOL_NAME)}"]) {
            def processScript = 'oc process'
            if (params) {
                processScript = processScript + ' ' + params.join(' ') 
            }
            if (paramFile) {
                processScript = processScript + ' --param-file ' + paramFile
            }
            processScript = processScript + ' -f ' + template + ' --local=true'
            def result = ctx.sh(script: processScript, returnStdout: true)
            ctx.echo("oc Process Template output:\n${result}")
            return result
        }
    }

}

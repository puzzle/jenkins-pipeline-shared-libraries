package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

import groovy.json.JsonOutput

class RocketSendCommand {

    private final PipelineContext ctx

    RocketSendCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- rocketSend --')
        Map<String, Object> data = new HashMap<>()
        data.put('text', ctx.stepParams.getRequired('message'))
        data.put('rawMessage', ctx.stepParams.getOptional('rawMessage', true))
        if (ctx.stepParams.getOptional('avatar') != null) {
            data.put('avatar', ctx.stepParams.getOptional('avatar'))
        }
        def webHook = ctx.stepParams.getRequired('webHook')

        def curlCmd = "curl " +
                "     -X POST \"" + webHook + "\" " +
                "     -H \"Content-Type: application/json\" " +
                "     --data '" + JsonOutput.toJson(data) + "' "

        int status = ctx.sh(script: curlCmd, returnStatus: true) as int
        if (status != 0) {
            ctx.fail('RocketChat notification failed!')
        } else {
            ctx.echo('RocketChat notification sent successfully')
        }
    }

}

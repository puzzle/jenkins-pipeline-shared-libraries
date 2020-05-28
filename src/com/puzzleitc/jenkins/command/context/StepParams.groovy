package com.puzzleitc.jenkins.command.context

class StepParams {

    private final Map params
    private final PipelineContext ctx

    StepParams(Map params, PipelineContext ctx) {
        this.params = params
        this.ctx = ctx
    }

    Object getRequired(String paramName) {
        Object value = params.get(paramName)
        if (!value) {
            ctx.fail("missing required step parameter: '${paramName}'")
        }
        return value
    }

    Object getOptional(String paramName, Object defaultValue) {
        return params.get(paramName) ?: defaultValue
    }

}

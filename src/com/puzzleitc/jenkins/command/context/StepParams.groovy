package com.puzzleitc.jenkins.command.context

class StepParams {

    private final Map params
    private final PipelineContext ctx

    StepParams(Map params, PipelineContext ctx) {
        this.params = params
        this.ctx = ctx
    }

    Object lookupRequired(String paramName) {
        Object value = params.get(paramName)
        if (!value) {
            ctx.fail("Missing required step parameter: '${paramName}'")
        }
        return value
    }

    Object lookupOptional(String paramName, Object defaultValue) {
        return params.get(paramName) ?: defaultValue
    }

}

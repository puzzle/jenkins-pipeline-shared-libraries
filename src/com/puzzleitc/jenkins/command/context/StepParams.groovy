package com.puzzleitc.jenkins.command.context

class StepParams {

    private final Map params
    private final PipelineContext ctx

    StepParams(Map params, PipelineContext ctx) {
        this.params = params
        this.ctx = ctx
    }

    Object getRequired(String paramName) {
        if (!params.containsKey(paramName)) {
            ctx.fail("missing required step parameter: '${paramName}'")
        }
        return params.get(paramName)
    }

    Object getOptional(String paramName) {
        return getOptional(paramName, null)
    }

    Object getOptional(String paramName, Object defaultValue) {
        return params.containsKey(paramName) ? params.get(paramName) : defaultValue
    }

    boolean contains(String paramName) {
        return params.containsKey(paramName)
    }

}

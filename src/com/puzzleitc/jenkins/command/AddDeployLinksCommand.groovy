package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext
import com.puzzleitc.jenkins.command.context.StepParams

class AddDeployLinksCommand {

    private final PipelineContext ctx

    AddDeployLinksCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- AddDeployLinks --')
        def deployJob = ctx.stepParams.getOptional('deployJob')
        if (deployJob == null) {
          error(ctx.getClass().getName() + ': No deploymentJob found. Must be specified!')
        }
        ctx.echo("deployJob: " + deployJob)
        def deploymentJob = Jenkins.instance.getItemByFullName(deployJob)
        if (deploymentJob == null) {
          error(ctx.getClass().getName() + ": can't find job '${deploymentJob}'!" )
        }
        ctx.addHtmlBadge html:"<a href=\"/${deploymentJob.getUrl()}parambuild?delay=0sec&built_name=${ctx.getEnv('JOB_NAME')}&built_number=${ctx.getEnv('BUILD_NUMBER')}\">Deploy</a> "
    }
}
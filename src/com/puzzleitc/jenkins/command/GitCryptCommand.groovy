package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class GitCryptCommand {

    private final PipelineContext ctx
    private String credentialsId

    GitCryptCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        credentialsId = ctx.stepParams.getOptional('credentialsId', null) as String
        if (credentialsId) {
            ctx.info("-- git-crypt unlock --")
            ctx.withCredentials([ctx.file(credentialsId: credentialsId, variable: 'GIT_CRYPT_KEYFILE')]) {
                ctx.sh script: 'git-crypt unlock $GIT_CRYPT_KEYFILE'
            }
        }
    }

    void cleanUp() {
        if (credentialsId) {
            ctx.info("-- git-crypt lock --")
            ctx.sh script: 'git-crypt lock'
        }
    }
}

package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class GitCryptCommand {

    private final PipelineContext ctx

    GitCryptCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def credentialsId = ctx.stepParams.getRequired('credentialsId') as String
        def body = ctx.stepParams.getRequired('body') as Closure
        def unlocked = false
        try {
            if (credentialsId) {
                ctx.info("-- git-crypt unlock --")
                ctx.withCredentials([ctx.file(credentialsId: credentialsId, variable: 'GIT_CRYPT_KEYFILE')]) {
                    ctx.sh script: 'git-crypt unlock $GIT_CRYPT_KEYFILE'
                    unlocked = true
                }
            }
            body()
        } finally {
            if (unlocked) {
                ctx.info("-- git-crypt lock --")
                ctx.sh script: 'git-crypt lock'
            }
        }
    }
}

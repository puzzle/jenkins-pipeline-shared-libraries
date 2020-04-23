package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.util.JenkinsInvoker

class KustomizeCommand {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

    private final String resource;

    KustomizeCommand(String resource) {
        this.resource = resource
    }

    Object execute() {
        def kustomizeHome = invoker.toolHome("kustomize")
        invoker.callWithEnv(["PATH+KUSTOMIZE_HOME=${kustomizeHome}/bin"]) {
            return invoker.sh(script: "kustomize build ${resource}", returnStdout: true)
        }
    }

}
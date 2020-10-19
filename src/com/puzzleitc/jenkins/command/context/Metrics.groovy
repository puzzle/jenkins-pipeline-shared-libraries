package com.puzzleitc.jenkins.command.context

import io.prometheus.client.Counter

class Metrics {

    static final Counter STEP_COUNTER = Counter.build()
            .name('shared_library_step_executions_total')
            .help('The total number of step executions in jenkins-pipeline-shared-libraries')
            .register()

    private Metrics() {
    }

}
package com.puzzleitc.jenkins.command.context

import io.prometheus.client.Collector
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter

class StepMetrics {

    private static final STEP_EXECUTION_COUNTER =
            findOrCreateCounter('shared_library_step_executions_total')

    private static Counter findOrCreateCounter(String name) {
        Counter counter = findCounter(name)
        if (counter == null) {
            return Counter.build().name(name).register()
        } else {
            return counter
        }
    }

    private static Counter findCounter(String name) {
        Set<Collector> collectors = CollectorRegistry.defaultRegistry.collectors()
        for (Collector collector : collectors) {
            if (collector instanceof Counter) {
                List<Collector.MetricFamilySamples> mfs = ((Counter) collector).describe()
                if (mfs.size() == 1 && name.equals(mfs.iterator().next().name)) {
                    return (Counter) collector
                }
            }
        }
        return null
    }

    void incrementStepExecutionCounter(String stepName) {
        STEP_EXECUTION_COUNTER.inc()
    }

}
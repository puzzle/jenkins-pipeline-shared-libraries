package com.puzzleitc.jenkins.command.context

import io.prometheus.client.Counter
import io.prometheus.client.Collector
import io.prometheus.client.CollectorRegistry
import java.util.List

class StepMetrics {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

    void increment() {

        Counter counter = findCounter('shared_library_step_executions_total')
        counter.inc()

        invoker.callEcho('Successfully incremented counter')
    }

    private Counter findOrCreateCounter(String name) {
        Counter counter = findCounter(name)
        if (counter == null) {
            return Counter.build().name(name).register()
        } else {
            return counter
        }
    }

    private Counter findCounter(String name) {
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

}
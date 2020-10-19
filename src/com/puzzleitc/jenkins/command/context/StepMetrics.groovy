package com.puzzleitc.jenkins.command.context

import io.prometheus.client.Counter
import io.prometheus.client.Collector
import io.prometheus.client.CollectorRegistry
import java.util.List

class StepMetrics {

    private final JenkinsInvoker invoker = new JenkinsInvoker()

    void increment() {
        invoker.callEcho('Gugus')

        Set<Collector> collectors = CollectorRegistry.defaultRegistry.collectors()
        for (Collector collector : collectors) {
            if (collector instanceof Collector.Describable) {
                List<Collector.MetricFamilySamples> mfs = ((Collector.Describable) m).describe()
                for (Collector.MetricFamilySamples family : mfs) {
                    invoker.callEcho("Collector: ${family.name}")
                }
            }
        }
    }

}
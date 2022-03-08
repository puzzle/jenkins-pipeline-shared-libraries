// Adopted from: https://github.com/SpencerMalone/JenkinsPipelineIntegration/blob/14ba88a59b2db7078a0f0a79c16935c8f3c1c3eb/generate-gradle-build.groovy
// Execute this script in "Script Console"
// on a running Jenkins to get plugin dependencies formatted to us in "build.gradle"
Jenkins.instance.pluginManager.plugins.each {
    def attributes = it.getManifest().getMainAttributes()
    if (attributes.getValue('Short-Name') != 'metrics' && attributes.getValue('Short-Name') != 'jobConfigHistory' && attributes.getValue('Short-Name') != 'jacoco') {
        def template = "testImplementation(\"${attributes.getValue('Group-Id')}:${attributes.getValue('Short-Name')}:${attributes.getValue('Plugin-Version')}\") {  artifact {name=\"${attributes.getValue('Short-Name')}\";type='jar'}; artifact {name=\"${attributes.getValue('Short-Name')}\";type='hpi' } }"
        println(template)
    }
}
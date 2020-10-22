package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class KustomizeSpec extends JenkinsPipelineSpecification {

    def kustomize = loadPipelineScriptForTest('vars/kustomize.groovy')

    def setup() {
        explicitlyMockPipelineStep('ansiColor')
        explicitlyMockPipelineStep('executable')
    }

    def 'it should call kustomize shell command'(args) {

        when:
        def result = kustomize.call(args)

        then:
        1 * getPipelineMock('executable').call(['name': 'kustomize']) >> '/path/bin'
        1 * getPipelineMock('sh').call({ it['script'].endsWith('/path/bin/kustomize build openshift/overlays/dev') && it['returnStdout'] }) >> 'kustomized output'
        result == 'kustomized output'

        where:
        args << [[path: 'openshift/overlays/dev'], 'openshift/overlays/dev']

    }

    def 'it should fail when path is not set'() {

        when:
        kustomize.call()

        then:
        thrown IllegalArgumentException

    }

}
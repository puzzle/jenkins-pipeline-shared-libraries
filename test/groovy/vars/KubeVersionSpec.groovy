package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import groovy.json.JsonSlurper

class KubeVersionSpec extends JenkinsPipelineSpecification {

    def kubeVersion = loadPipelineScriptForTest('vars/kubeVersion.groovy')

    def setup() {
        explicitlyMockPipelineStep('executable')
    }

    def 'it should fail when parameter is not set'(args) {

        when:
        kubeVersion.call(args)

        then:
        thrown IllegalArgumentException

        where:
        args << [[missing : 'server']]

    }

    def 'it should fail with wrong parameter'(args) {

        when:
        kubeVersion.call(args)

        then:
        thrown IllegalArgumentException

        where:
        args << [['cluster' : "myCluster"]]

    }

}
package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class OpenshiftProcessSpec extends JenkinsPipelineSpecification {

    def openshiftProcess = loadPipelineScriptForTest('vars/openshiftProcess.groovy')

    def setup() {
        explicitlyMockPipelineStep('executable')
    }

    def 'it should fail when templateFilePath is not set'() {

        when:
        openshiftProcess.call()

        then:
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock("error").call('Build failed')

    }

    def 'it should call install oc tool'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml --local=true --ignore-unknown-parameters=false --output=json'

        setup:
        openshiftProcess.getBinding().setVariable('env', [:])

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 1
        1 * getPipelineMock('executable').call(['name':'oc','toolName':'oc_3_11']) >> '/path/bin'
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml']]

    }

    def 'it should call oc process with template'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml --local=true --ignore-unknown-parameters=false --output=json'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml']]

    }

    def 'it should call oc process with params'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml KEY1=value1 KEY2=value2 --local=true --ignore-unknown-parameters=false --output=json'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', params: [ 'KEY1=value1', 'KEY2=value2' ]]]

    }

    def 'it should call oc process with paramFile'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml --param-file=openshift/environments/dev.yaml --local=true --ignore-unknown-parameters=false --output=json'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', paramFile: 'openshift/environments/dev.yaml']]

    }

    def 'it should call oc process with ignoreUnknownParameters'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml --local=true --ignore-unknown-parameters=true --output=json'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', ignoreUnknownParameters: true]]

    }

    def 'it should call oc process with labels'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml -l app=my-app -l deploy=two --local=true --ignore-unknown-parameters=false --output=json'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', labels: [ 'app=my-app', 'deploy=two' ]]]

    }

    def 'it should call oc process with output'(args) {

        def expectedCommand = 'oc process -f openshift/templates/dev-template.yaml --local=true --ignore-unknown-parameters=false --output=yaml'

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('echo').call('template file: openshift/templates/dev-template.yaml')
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock('sh').call({ it['script'].endsWith(expectedCommand) && it['returnStdout'] }) >> 'openshiftProcessed output'
        result == 'openshiftProcessed output'

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', output: 'yaml']]

    }

    def 'it should fail with unsupported output format'(args) {

        when:
        def result = openshiftProcess.call(args)

        then:
        1 * getPipelineMock('sh').call({ it['script'].endsWith('command -v oc') && it['returnStatus'] }) >> 0
        1 * getPipelineMock("error").call('Build failed')

        where:
        args << [[templateFilePath: 'openshift/templates/dev-template.yaml', output: 'jsonpath']]

    }

}
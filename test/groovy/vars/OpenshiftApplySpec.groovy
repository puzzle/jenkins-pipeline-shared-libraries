package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import groovy.json.JsonSlurper

class OpenshiftApplySpec extends JenkinsPipelineSpecification {

    def openshiftApply = loadPipelineScriptForTest('vars/openshiftApply.groovy')

    def setup() {
        explicitlyMockPipelineStep('executable')
    }

    def 'it should fail when parameter is not set'(args) {

        when:
        openshiftApply.call(args)

        then:
        1 * getPipelineMock("echo").call({ it.contains("missing required step parameter: '${args.missing}'") })
        1 * getPipelineMock("error").call('Build failed') >> { throw new RuntimeException() }
        thrown(RuntimeException)

        where:
        args << [[project : "myproject",
                  appLabel: "label",
                  missing : "configuration"],
                 [configuration: "configuration",
                  appLabel     : "label",
                  missing      : "project"],
                 [project      : "myproject",
                  configuration: "configuration",
                  missing      : "appLabel"]]

    }

    def 'it calls install oc4 tool and oc convert'(args) {

        def output = '{ "operation": "describe", "actions": [ { "err": "", "verb": "describe", "cmd": "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX", "out": "Name: test-app\\nNamespace: myproject\\nLabels: pp=label\\nSelector: app=label", "status": 0 } ], "status": 0}'
        output = new JsonSlurper().parseText(output)

        setup:
        openshiftApply.getBinding().setVariable('env', [:])
        openshiftApply.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftApply.call(args)

        then:
        2 * getPipelineMock('sh').call({ ['script': 'command -v oc', 'returnStatus': 'true'] }) >> 1
        1 * getPipelineMock('executable').call(['name': 'oc', 'toolName': 'oc4']) >> '/path/bin'
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.raw').call(['convert', '-f', 'null/temp.yaml']) >> [actions: [[cmd: "oc --server=https://openshift.puzzle.ch --insecure-skip-tls-verify --namespace=pitc-jenkinscicd-prod --token=XXXXX  convert -f /var/lib/jenkins/workspace/sgilgen/shared-library-test/1636550271093/temp.yaml"]]]
        1 * getPipelineMock('openshift.apply').call([args.configuration, '-l', 'app=label', '--prune']) >> output

        where:
        args << [[configuration : "[ { apiVersion: 'extensions/v1beta1', kind: 'Deployment', spec: { template: { spec: { containers: [ { name: 'mycontainer', }, ], }, }, }, }, ]",
                  project       : "myproject",
                  cluster       : "oc4-test-cluster",
                  appLabel      : "label",
                  waitForRollout: false]]

    }

    def 'it should not call oc validate for oc4'(args) {

        def output = '{ "operation": "describe", "actions": [ { "err": "", "verb": "describe", "cmd": "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX", "out": "Name: test-app\\nNamespace: myproject\\nLabels: pp=label\\nSelector: app=label", "status": 0 } ], "status": 0}'
        output = new JsonSlurper().parseText(output)

        setup:
        openshiftApply.getBinding().setVariable('env', [:])
        openshiftApply.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftApply.call(args)

        then:
        1 * getPipelineMock('sh')({ [script: 'command -v oc', returnStatus: true] }) >> 0
        1 * getPipelineMock('sh')(_) >> { _arguments ->
            assert '#!/bin/sh -e\noc version -o json >/dev/null 2>&1' == _arguments[0]['script']
            assert true == _arguments[0]['returnStatus']
            return 0
        }
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.raw')(_) >> { _arguments ->
            assert 'apply' == _arguments[0][0]
            assert '-f' == _arguments[0][1]
            assert 'null/temp.yaml' == _arguments[0][2]
            assert '--dry-run=server' == _arguments[0][3]
            return [actions: [[cmd: "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX  apply -f /temp.yaml"]]]
        }
        1 * getPipelineMock('openshift.apply').call([args.configuration, '-l', 'app=label', '--prune']) >> output

        where:
        args << [[configuration : "[ { apiVersion: 'extensions/v1beta1', kind: 'Deployment', spec: { template: { spec: { containers: [ { name: 'mycontainer', }, ], }, }, }, }, ]",
                  project       : "myproject",
                  cluster       : "oc4-test-cluster",
                  appLabel      : "label",
                  waitForRollout: false]]

    }

    def 'it should wait for rollout with default selector'(args) {

        def output = '{ "operation": "describe", "actions": [ { "err": "", "verb": "describe", "cmd": "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX", "out": "Name: test-app\\nNamespace: myproject\\nLabels: pp=label\\nSelector: app=label", "status": 0 } ], "status": 0}'
        output = new JsonSlurper().parseText(output)

        // mock RolloutManager
        def myRollout = Mock(RolloutManager)
        // mock Selector to return the RolloutManager mock and check the method is called once
        def mySelector = Mock(Selector) {
            1 * rollout() >> myRollout
        }

        setup:
        openshiftApply.getBinding().setVariable('env', [:])
        openshiftApply.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftApply.call(args)

        then:
        1 * getPipelineMock('sh')({ [script: 'command -v oc', returnStatus: true] }) >> 0
        1 * getPipelineMock('sh')(_) >> { _arguments ->
            assert '#!/bin/sh -e\noc version -o json >/dev/null 2>&1' == _arguments[0]['script']
            assert true == _arguments[0]['returnStatus']
            return 0
        }
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.raw').call(['apply', '-f', 'null/temp.yaml', '--dry-run=server']) >> [actions: [[cmd: "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX  apply -f /temp.yaml"]]]
        1 * getPipelineMock('openshift.apply').call([args.configuration, '-l', 'app=label', '--prune']) >> output
        1 * getPipelineMock('echo')("waiting for 'deployment' with selector 'label' to be rolled out")
        1 * getPipelineMock('openshift.selector')(_) >> { _arguments ->
            assert 'deployment' == _arguments[0][0]
            assert 'label' == _arguments[0][1]
            return mySelector  // the Selector mock returning the RolloutManager mock on the rollout method
        }

        where:
        args << [[configuration : "[ { apiVersion: 'extensions/v1beta1', kind: 'Deployment', spec: { template: { spec: { containers: [ { name: 'mycontainer', }, ], }, }, }, }, ]",
                  project       : "myproject",
                  cluster       : "oc4-test-cluster",
                  appLabel      : "label",
                  rolloutKind   : "deployment",
                  waitForRollout: true]]

    }

    def 'it should wait for rollout with rolloutSelector'(args) {

        def output = '{ "operation": "describe", "actions": [ { "err": "", "verb": "describe", "cmd": "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX", "out": "Name: test-app\\nNamespace: myproject\\nLabels: pp=label\\nSelector: app=label", "status": 0 } ], "status": 0}'
        output = new JsonSlurper().parseText(output)

        // mock RolloutManager
        def myRollout = Mock(RolloutManager)
        // mock Selector to return the RolloutManager mock and check the method is called once
        def mySelector = Mock(Selector) {
            1 * rollout() >> myRollout
        }

        setup:
        openshiftApply.getBinding().setVariable('env', [:])
        openshiftApply.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftApply.call(args)

        then:
        1 * getPipelineMock('sh')({ [script: 'command -v oc', returnStatus: true] }) >> 0
        1 * getPipelineMock('sh')(_) >> { _arguments ->
            assert '#!/bin/sh -e\noc version -o json >/dev/null 2>&1' == _arguments[0]['script']
            assert true == _arguments[0]['returnStatus']
            return 0
        }
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.raw').call(['apply', '-f', 'null/temp.yaml', '--dry-run=server']) >> [actions: [[cmd: "oc --server=https://openshift.openshift.com --insecure-skip-tls-verify --namespace=myproject --token=XXXXX  apply -f /temp.yaml"]]]
        1 * getPipelineMock('openshift.apply').call([args.configuration, '-l', 'app=label', '--prune']) >> output
        1 * getPipelineMock('echo')("waiting for 'deployment' with selector [test:selector] to be rolled out")
        1 * getPipelineMock('openshift.selector')(_) >> { _arguments ->
            assert 'deployment' == _arguments[0][0]
            assert [test: 'selector'] == _arguments[0][1]
            return mySelector  // the Selector mock returning the RolloutManager mock on the rollout method
        }

        where:
        args << [[configuration : "[ { apiVersion: 'extensions/v1beta1', kind: 'Deployment', spec: { template: { spec: { containers: [ { name: 'mycontainer', }, ], }, }, }, }, ]",
                  project       : "myproject",
                  cluster       : "oc4-test-cluster",
                  appLabel      : "label",
                  rolloutKind   : "deployment",
                  waitForRollout: true,
                  rolloutSelector: [test: 'selector']
                 ]]
    }

}
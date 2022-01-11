package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class OpenshiftStartBuildCommandSpec extends JenkinsPipelineSpecification {

    /**
     * TODO: test startBuild command
     */

    def openshiftStartBuild = loadPipelineScriptForTest('vars/openshiftStartBuild.groovy')

    def setup() {
        explicitlyMockPipelineStep('executable')
    }

    def 'it should fail when parameter is not set'(args) {

        when:
        openshiftStartBuild.call(args)

        then:
        1 * getPipelineMock("echo").call({ it.contains("missing required step parameter: '${args.missing}'") })
        1 * getPipelineMock("error").call('Build failed') >> { throw new RuntimeException() }
        thrown(RuntimeException)

        where:
        args << [[project        : "myProject",
                  cluster        : "myCluster",
                  missing        : "buildConfigName"],
                 [cluster        : "myCluster",
                  buildConfigName: "buildConfig",
                  missing        : "project"],
                 [project        : "myProject",
                  buildConfigName: "buildConfig",
                  missing        : "cluster"]]

    }

    def 'it calls startBuild'(args) {

        // mock Result
        def myResult = new Result().with {
            actions = [[cmd: "oc startBuild ..."]]
            status = 0
            it
        }

        // mock Build to return the Result mock and check that the method is called once
        def myBuild = Mock(Build) {
            1 * logs(_) >> myResult
            1 * object() >> [status: [phase: "Success"]]
        }
        // mock BuildConfig to return the Build mock and check that the method is called once
        def myBuildConfig = Mock(BuildConfig) {
            1 * startBuild(_) >> myBuild
        }

        setup:
        openshiftStartBuild.getBinding().setVariable('env', [:])
        openshiftStartBuild.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftStartBuild.call(args)

        then:
        1 * getPipelineMock('sh').call({ ['script': 'command -v oc', 'returnStatus': 'true'] }) >> 0
        0 * getPipelineMock('executable').call(['name': 'oc', 'toolName': 'oc_3_11']) >> '/path/bin'
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.selector')(_) >> { _arguments ->
            assert 'bc/buildConfig' == _arguments[0]
            return myBuildConfig  // the BuildConfig mock providing the startBuild method
        }

        where:
        args << [[project         : "myProject",
                  cluster         : "oc4-test-cluster",
                  buildConfigName : "buildConfig"]]

    }

    def 'it calls install oc_3_11 tool and startBuild'(args) {

        // mock Result
        def myResult = new Result().with {
            actions = [[cmd: "oc startBuild ..."]]
            status = 0
            it
        }

        // mock Build to return the Result mock and check that the method is called once
        def myBuild = Mock(Build) {
            1 * logs(_) >> myResult
            1 * object() >> [status: [phase: "Success"]]
        }
        // mock BuildConfig to return the Build mock and check that the method is called once
        def myBuildConfig = Mock(BuildConfig) {
            1 * startBuild(_) >> myBuild
        }

        setup:
        openshiftStartBuild.getBinding().setVariable('env', [:])
        openshiftStartBuild.getBinding().setVariable('secretValue', 'dGVzdA==')

        when:
        openshiftStartBuild.call(args)

        then:
        1 * getPipelineMock('sh').call({ ['script': 'command -v oc', 'returnStatus': 'true'] }) >> 1
        1 * getPipelineMock('executable').call(['name': 'oc', 'toolName': 'oc_3_11']) >> '/path/bin'
        1 * getPipelineMock('openshift.withCluster')(_)
        1 * getPipelineMock('openshift.withProject')(_)
        1 * getPipelineMock('openshift.withCredentials')(_)
        1 * getPipelineMock('openshift.raw').call('whoami') >> [out: "jenkins"]
        1 * getPipelineMock('openshift.selector')(_) >> { _arguments ->
            assert 'bc/buildConfig' == _arguments[0]
            return myBuildConfig  // the BuildConfig mock providing the startBuild method
        }

        where:
        args << [[project         : "myProject",
                  cluster         : "oc4-test-cluster",
                  buildConfigName : "buildConfig"]]

    }

}
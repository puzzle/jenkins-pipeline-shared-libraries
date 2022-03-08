package groovy.vars

// TODO: Refactor using JUnit
//import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
//
//class OpenshiftUtilsSpec extends JenkinsPipelineSpecification {
//
//    def openshiftUtils = loadPipelineScriptForTest('vars/openshiftUtils.groovy')
//
//    def 'Applys the template without namespace'() {
//        setup:
//            explicitlyMockPipelineVariable('openshift_token')
//        when:
//            openshiftUtils.applyTemplate("https://console.appuio.ch/", "my-appuio-project", "template.yaml", "APPUiO_login_token", false)
//        then:
//            1 * getPipelineMock('echo').call('OpenShift server URL: https://console.appuio.ch/')
//            1 * getPipelineMock('echo').call('OpenShift project: my-appuio-project')
//            1 * getPipelineMock('echo').call('resource file: template.yaml')
//            1 * getPipelineMock('withCredentials').call(_) >> { _arguments ->
//                def credentialsId = 'APPUiO_login_token'
//                credentialsId == _arguments[0][0].credentialsId[0]
//            }
//            1 * getPipelineMock('tool').call(_) >> '/home/jenkins'
//            1 * getPipelineMock('withEnv').call(_) >> { _arguments ->
////              TODO: pwd mock
//                def env = ["KUBECONFIG=null/test/path/.kube", "PATH+OC_HOME=/home/jenkins/bin", "ocpUrl=https://console.appuio.ch/"]
//                env == _arguments[0][0]
//            }
//            1 * getPipelineMock('sh').call('oc login https://console.appuio.ch/ --insecure-skip-tls-verify=true --token=Mock Generator for [openshift_token]')
//            1 * getPipelineMock('sh').call('oc process -f template.yaml | oc apply -f -')
//    }
//
//    def 'Applys the template with namespace'() {
//        setup:
//            explicitlyMockPipelineVariable('openshift_token')
//        when:
//            openshiftUtils.applyTemplate("https://console.appuio.ch/", "my-appuio-project", "template.yaml", "APPUiO_login_token", true)
//        then:
//            1 * getPipelineMock('sh').call('oc process -f template.yaml -p NAMESPACE_NAME=$(oc project -q) | oc apply -f -')
//    }
//
//    def 'Applys the template with envFile and without namespace'() {
//        setup:
//            explicitlyMockPipelineVariable('openshift_token')
//        when:
//            openshiftUtils.applyTemplateWithEnvFile("https://console.appuio.ch/", "my-appuio-project", "template.yaml", "APPUiO_login_token", 'envFile.env', false)
//        then:
//            1 * getPipelineMock('echo').call('environment file: envFile.env')
//            1 * getPipelineMock('sh').call('oc process -f template.yaml --param-file envFile.env | oc apply -f -')
//    }
//
//    def 'Applys the template with namespace and envFile'() {
//        setup:
//            explicitlyMockPipelineVariable('openshift_token')
//        when:
//            openshiftUtils.applyTemplateWithEnvFile("https://console.appuio.ch/", "my-appuio-project", "template.yaml", "APPUiO_login_token", 'envFile.env', true)
//        then:
//            1 * getPipelineMock('sh').call('oc process -f template.yaml -p NAMESPACE_NAME=$(oc project -q) --param-file envFile.env | oc apply -f -')
//    }
//}

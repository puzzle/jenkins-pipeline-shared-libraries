//
// OpenShift resource update by applying a template.
//
// method parameters:
// ocpUrl -> url of the OpenShift server
// ocpProject -> project-name/namespace of the build project
// templateFile -> OpenShift template
// credentialsId -> credentials for the OpenShift login
// namespace -> true adds NAMESPACE_NAME param
//
def void applyTemplate(String ocpUrl, String ocpProject, String templateFile, String credentialsId, boolean namespace) {

    echo "-- start resource update by template --"
    echo "OpenShift server URL: $ocpUrl"
    echo "OpenShift project: $ocpProject"
    echo "resource file: $templateFile"

    withCredentials([[$class        : 'StringBinding',
                      credentialsId   : "${credentialsId}",
                      variable        : 'openshift_token']]) {
        withEnv(["KUBECONFIG=${pwd()}/.kube", "PATH+OC_HOME=${tool 'oc'}/bin", "ocpUrl=${ocpUrl}"]) {
            sh "oc login $ocpUrl --insecure-skip-tls-verify=true --token=$openshift_token"
            sh "oc project $ocpProject"
            sh "oc project"
            sh "oc whoami"

            // apply template
            if (namespace) {
                sh "oc process -f $templateFile -p NAMESPACE_NAME=\$(oc project -q) | oc apply -f -"
            } else {
                sh "oc process -f $templateFile | oc apply -f -"
            }
        }
    }
}

//
// OpenShift resource update by applying a template with environment file.
//
// method parameters:
// ocpUrl -> url of the OpenShift server
// ocpProject -> project-name/namespace of the build project
// templateFile -> OpenShift template
// credentialsId -> credentials for the OpenShift login
// envFile -> environment file
// namespace -> true adds NAMESPACE_NAME param
//
def void applyTemplateWithEnvFile(String ocpUrl, String ocpProject, String templateFile, String credentialsId, String envFile, boolean namespace) {

    echo "-- start resource update by template with environment file --"
    echo "OpenShift server URL: $ocpUrl"
    echo "OpenShift project: $ocpProject"
    echo "resource file: $templateFile"
    echo "environment file: $envFile"

    withCredentials([[$class        : 'StringBinding',
                      credentialsId   : "${credentialsId}",
                      variable        : 'openshift_token']]) {
        withEnv(["KUBECONFIG=${pwd()}/.kube", "PATH+OC_HOME=${tool 'oc'}/bin", "ocpUrl=${ocpUrl}"]) {
            sh "oc login $ocpUrl --insecure-skip-tls-verify=true --token=$openshift_token"
            sh "oc project $ocpProject"
            sh "oc project"
            sh "oc whoami"

            // apply template
            if (namespace) {
                sh "oc process -f $templateFile -p NAMESPACE_NAME=\$(oc project -q) --param-file $envFile | oc apply -f -"
            } else {
                sh "oc process -f $templateFile --param-file $envFile | oc apply -f -"
            }
        }
    }
}

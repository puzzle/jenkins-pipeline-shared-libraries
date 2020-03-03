//
// OpenShift resource update with param
//
// method parameters:
// ocpUrl -> url of the OpenShift server
// ocpProject -> project-name/namespace of the build project
// file -> resource definition
// credentialsId -> credentials for the OpenShift login
// envFile -> environment file
// namespace -> true adds NAMESPACE_NAME param
//
def void updateResourcesWithEnvFile(String ocpUrl, String ocpProject, String file, String credentialsId, String envFile, boolean namespace) {

    echo "-- start resource update with environment form file --"
    echo "OpenShift server URL: $ocpUrl"
    echo "OpenShift project: $ocpProject"
    echo "resource file: $file"
    echo "environment file: $envFile"

    withCredentials([[$class        : 'UsernamePasswordMultiBinding',
                      credentialsId   : "${credentialsId}",
                      passwordVariable: 'openshift_password',
                      usernameVariable: 'openshift_username']]) {
        withEnv(["KUBECONFIG=${pwd()}/.kube", "PATH+OC_HOME=${tool 'oc'}/bin", "ocpUrl=${ocpUrl}"]) {
            sh "oc login $ocpUrl --insecure-skip-tls-verify=true --token=$openshift_password"
            sh "oc project $ocpProject"
            sh "oc project"
            sh "oc whoami"

            // apply template
            if (namespace) {
                sh "oc process -f $file -p NAMESPACE_NAME=\$(oc project -q) --param-file $envFile | oc apply -f -"
            } else {
                sh "oc process -f $file --param-file $envFile | oc apply -f -"
            }
        }
    }
}

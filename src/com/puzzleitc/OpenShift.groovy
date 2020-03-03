package com.puzzleitc

def tar(openShiftConfigDir, String... deployables) {
  String potHome = tool("pot")
  sh "${potHome}/bin/pot tar ${openShiftConfigDir} ${deployables.join(' ')}"
  archive 'deployment.tar.gz'
}

def deploy(Map args) {
   echo "Deploying build ${args['build']} to tier ${args['tier']}."
   copyArtifact(args['build'], "deployment.tar.gz")

   exportVars(args)

   String potHome = tool("pot")
   sh "${potHome}/bin/pot deploy ${args['tier']} ${args['options']}"
}

def exportVars(Map args) {
   // Export variables to environment for Puzzle OpenShift Tool
   for (arg in args) {
     env["POT_" + arg.key.replaceAll(/(\B[A-Z])/, /_$1/).toUpperCase()] = arg.value
   }
}

def copyArtifact(String build, String artifact) {
   def matcher = build =~ '/job/([^/]+)/([0-9]+)'
   def job = matcher[0][1]
   def buildNumber = matcher[0][2]

   sh "cp -f ${env.JENKINS_HOME}/jobs/${job}/builds/${buildNumber}/archive/${artifact} ."
}

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

return this

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

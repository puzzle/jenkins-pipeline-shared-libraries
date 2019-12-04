package com.puzzleitc.jenkins


import groovy.json.JsonSlurperClassic

/**
 * Jenkins JDK upstream information.
 */
class AvailableJDKs {

  /**
   * Reads the JDK file of Jenkins that provides the information of the available JDK's.
   * JDK file location: JENKINS_HOME/updates/hudson.tools.JDKInstaller
   *
   * @return the information to the available JDK's as Map
   */
  @com.cloudbees.groovy.cps.NonCPS
  static Map jdks() {
    def inputFile = new File(System.getProperty("JENKINS_HOME") +"/updates/hudson.tools.JDKInstaller")
    def json = new JsonSlurperClassic().parseText(inputFile.text)
    Map jdks = new TreeMap()

    for (Map data : json.data) {
      Map release = data.releases[0]
        for (Map file : release.files) {
          if (file.filepath =~ /(?i)linux.*(x64|x86_64|amd64)/) {
            def matcher = file.filepath =~ /\/jdk-([^-_]+)/
            jdks[data.name] = ['url': file.filepath, 'version': matcher[0][1]]
          }
        }
    }
    return jdks
  }
}

#!/usr/bin/env groovy
import hudson.model.Cause
import org.jenkinsci.plugins.pipeline.maven.publishers.MavenReport

Map readComponentVersionsFromArtifact(String job, Object selector, String file) {
  try {
    copyArtifacts filter: file, projectName: job, selector: selector
    def versions = readYaml(file: file)
    if (versions instanceof Map) {
      return versions
    } else {
      return [:]
    }
  // when no artifact available (e.g. its the first run) 
  // return an empty map instead of throwing an error
  } catch (Exception e) {
    return [:]
  }
}

void writeComponentVersionsToArtifact(String dataFile, Map componentInfos) {
  // always remove the old file first
  sh "rm -f ${dataFile}"
  writeYaml file: dataFile, data: componentInfos
  archiveArtifacts dataFile
}

def getUpstreamCauses() {
  def result = []

  def buildCauses = currentBuild.rawBuild.getCauses()
  buildCauses.each {
    if (it instanceof Cause.UpstreamCause) {
      def upstreamCause = it as Cause.UpstreamCause
      result << [job: upstreamCause.upstreamProject, build: upstreamCause.upstreamBuild.toString()]
    }
  }

  return result
}

def getComponentInformations(String componentVersion, Boolean externalComponent = false) {
  def infos = [version: componentVersion + '-' + env.BUILD_NUMBER, job: env.JOB_NAME, buildNumber: env.BUILD_NUMBER as int, buildUrl: env.BUILD_URL]
  if(externalComponent) {
    infos = [version: componentVersion]
  }

  // does the current build contain a maven build?
  def mavenReport = currentBuild.rawBuild.getActions(MavenReport.class)
  if (mavenReport) {
    def mavenArtifacts = []
    mavenReport[0].getDeployedArtifacts().each {
      mavenArtifacts << it.url
    }
    infos['artifacts'] = mavenArtifacts
  }

  return infos
}

def call(Map args) {
  final COMPONENT_VERSIONS_FILE = 'component-versions.yaml'

  lock(resource: 'trackComponentVersions', inversePrecedence: false) {
    def componentInfos = readComponentVersionsFromArtifact(env.JOB_NAME, lastSuccessful(), COMPONENT_VERSIONS_FILE)

    // If a build is taking a long time multiple other build requests can 
    // occour so there may be multiple build causes: http://javadoc.jenkins-ci.org/hudson/model/Run.html#getCauses--
    getUpstreamCauses().each {
      def upstreamVersions = readComponentVersionsFromArtifact(it.job, specific(it.build), COMPONENT_VERSIONS_FILE)
      componentInfos.putAll(upstreamVersions)
    }
    // check if pomFile location is passed
    if (args?.pomFile) {
      def mavenCoordinates = readMavenPom(file: args.pomFile)
      componentInfos[mavenCoordinates.artifactId] = getComponentInformations(mavenCoordinates.version)
    // is version yaml passed?
    } else if (args?.versionFile) {
      releaseVersion = readYaml(file: args.versionFile)
      if (args?.containsExternalComponents) {
        releaseVersion.each { k, v -> componentInfos[k] = getComponentInformations(v, args.containsExternalComponents) }
      } else {
        releaseVersion.each { k, v -> componentInfos[k] = getComponentInformations(v) }
      }
    } else {
      error(getClass().getName() + ': Either pomFile or versionFile must be set!')
    }

    writeComponentVersionsToArtifact(COMPONENT_VERSIONS_FILE, componentInfos)
  }
}

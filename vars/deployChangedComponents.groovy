#!/usr/bin/env groovy

import hudson.model.Run
import com.jenkinsci.plugins.badge.action.AbstractAction
import com.jenkinsci.plugins.badge.action.BadgeAction
import com.jenkinsci.plugins.badge.action.BadgeSummaryAction

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

void writeComponentVersionsToArtifact(String dataFile, Map componentVersions) {
  // always remove the old file first
  sh "rm -f ${dataFile}"
  writeYaml file: dataFile, data: componentVersions
  archiveArtifacts dataFile
}

// always remove the old badge action before appling anything new
void removeBadgeAction(Run currentBuild, Class actionClass, String id) {
  def badgeAction = currentBuild.getActions(actionClass).find { it.id == id }
  if (badgeAction != null) {
    currentBuild.removeAction(badgeAction)
  }
}

void setBadgeInBuildHistory(Run currentBuild, String icon, String altText, String id, String link) {
  removeBadgeAction(currentBuild, BadgeAction.class, id)

  // as the badge-plugin does not support the adding of badges
  // to other builds, we have to use the class directly
  def badgeAction = BadgeAction.createBadge(icon, altText, link)
  badgeAction.setId(id)
  currentBuild.addAction(badgeAction)
}

void setBadgeAndLinkInSummary(Run currentBuild, String icon, String id, String altText) {
  removeBadgeAction(currentBuild, BadgeSummaryAction.class, id)

  // as the badge-plugin does not support the adding of badges
  // to other builds, we have to use the class directly
  def badgeSummaryAction = new BadgeSummaryAction(icon)
  badgeSummaryAction.setId(id)
  badgeSummaryAction.appendText(altText)
  currentBuild.addAction(badgeSummaryAction)
}

// Depending on the Badgelocation the Icon is either "large" or "small"
String getDeployIcon(String targetEnv, boolean isLarge = false) {
    size = "16x16"
    if (isLarge) {
      size = "32x32"
    }
    if (targetEnv == "test") {
        return '/plugin/promoted-builds/icons/' + size + '/star-silver.png'
    } else if (targetEnv == "prod") {
        return '/plugin/promoted-builds/icons/' + size + '/star-gold.png'
    } else if (targetEnv == "int") {
        return '/plugin/promoted-builds/icons/' + size + '/star-purple.png'
    }
    return '/plugin/promoted-builds/icons/' + size + '/star-orange.png'
}

void addDeployedBadges() {
  def built = Jenkins.instance.getItemByFullName(built_name).getBuild(built_number)
  def deploy = currentBuild.rawBuild

  setBadgeInBuildHistory(deploy, getDeployIcon(target_env), "Deployed ${built_name} #${built_number} to ${target_env}", target_env, "/${built.getUrl()}")
  setBadgeAndLinkInSummary(deploy, getDeployIcon(target_env, true), target_env, "Deployed <a href=\"/${built.getParent().getUrl()}\">${built_name}</a> <a href=\"/${built.getUrl()}\">#${built_number}</a> to ${target_env}")

  setBadgeInBuildHistory(built, getDeployIcon(target_env), "Deployed to ${target_env} by ${env.JOB_NAME} #${env.BUILD_NUMBER}", target_env, "/${currentBuild.rawBuild.getUrl()}")
  setBadgeAndLinkInSummary(built, getDeployIcon(target_env, true), target_env, "Deployed to ${target_env} by <a href=\"/${currentBuild.rawBuild.getParent().getUrl()}\">${env.JOB_NAME}</a> <a href=\"/${currentBuild.rawBuild.getUrl()}\">#${env.BUILD_NUMBER}</a>")
  // save is required to persist badges on other builds than the current
  built.save()

  built.keepLog(true)
}

def call() {
  final DEPLOYED_VERSIONS_FILE = 'deployed-versions.yaml'
  final COMPONENT_VERSIONS_FILE = 'component-versions.yaml'
  final EXECUTION_JOB_FILE = 'rundeck-jobs.yaml'

  def newComponentVersions = readComponentVersionsFromArtifact(built_name, specific(built_number), COMPONENT_VERSIONS_FILE)
  def deployedVersions = readComponentVersionsFromArtifact(env.JOB_NAME, lastSuccessful(), DEPLOYED_VERSIONS_FILE)

  def currentComponentVersions = deployedVersions[target_env]
  deployedVersions[target_env] = newComponentVersions

  executionJobs = readYaml(file: EXECUTION_JOB_FILE)

  newComponentVersions.each { k, v ->

    if (!currentComponentVersions) {
      echo "Installing component ${k} on environment ${target_env} with version ${v.version}."
      echo "Executing rundeck job ${executionJobs[k].jobId} with env ${target_env} and version ${v.version}."  // TODO: Replace with rundeck call
    } else if (v.version == currentComponentVersions[k].version) {
      echo "Component ${k} version ${v.version} already deployed, skipping."
    } else {
      echo "Updating component ${k} on environment ${target_env} from version ${currentComponentVersions[k].version} to ${v.version}."
      echo "Executing rundeck job ${executionJobs[k].jobId} with env ${target_env} and version ${v.version}."  // TODO: Replace with rundeck call
    }
  }

  writeComponentVersionsToArtifact DEPLOYED_VERSIONS_FILE, deployedVersions
  addDeployedBadges()
}

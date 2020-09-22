#!/usr/bin/env groovy

import com.jenkinsci.plugins.badge.action.BadgeAction

// the function implements how many build per environment should be kept
// parameter is maxKeepBuilds
def call(Map args) {
  def maxNumberToKeepBuilds = args?.maxKeepBuilds ?: 10;

  def environmentBuildCount = [:]
  Jenkins.instance.getItemByFullName(args.job)
    .getBuilds()
    .findAll { it.isKeepLog() }
    .each { build ->
      deployedEnvironment = []
      build.getActions(BadgeAction.class).each {
        deployedEnvironment << it.id
        environmentBuildCount[it.id] = environmentBuildCount.get(it.id, 0) + 1
      }

      // each Build that should be kept will be stored in keepBuild map
      def keepBuild = []
      deployedEnvironment.each {
        if (environmentBuildCount[it] <= maxNumberToKeepBuilds) {
          keepBuild << it
        }
      }

      // print out reason of/not keeping the build
      if (keepBuild) {
        echo "Keeping build ${build} because of the following promotions: ${keepBuild.join(' ')}"
      } else {
        echo "Deleting build ${build}"
        build.delete()
      }
    }
}

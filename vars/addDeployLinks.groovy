#!/usr/bin/env groovy

def call(Map args) {
  if (args?.deployJob) {
    def deploymentJob = Jenkins.instance.getItemByFullName(args.deployJob)
    if (!deploymentJob) {
      error(getClass().getName() + ": can't find job '${args.deploymentJob}'!" )
    }
    addHtmlBadge html:"<a href=\"/${deploymentJob.getUrl()}parambuild?delay=0sec&built_name=${env.JOB_NAME}&built_number=${env.BUILD_NUMBER}\">Deploy</a> "
  } else {
    error(getClass().getName() + ': No deploymentJob found. Must be specified!')
  }
}

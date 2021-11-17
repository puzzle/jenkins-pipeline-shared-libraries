# jenkins-pipeline-shared-libraries

Shared functionality for Jenkins Pipeline Groovy scripts.

Included by the [Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin)

Documentation: <https://jenkins.io/doc/book/pipeline/shared-libraries/>

## global variables

### addDeployLinks

Adds a link(to another job) for a specific build from the build history of a jenkins job.
See it's [documentation](vars/addDeployLinks.txt)

Source: [vars/addDeployLinks.groovy](vars/addDeployLinks.groovy)

### cleanBuilds

Cleans up the build history of the jenkins job. It can differentiate deployments to specific environments.
See it's [documentation](vars/cleanBuilds.txt)

Source: [vars/cleanBuilds.groovy](vars/cleanBuilds.groovy)

### deployChangedComponents

This function can be used as a template for deploying a component on a specifc environment.
There is no actual implementation of a deployment but a placeholder with an echo output.
See it's [documentation](vars/deployChangedComponents.txt)

Source: [vars/deployChangedComponents.groovy](vars/deployChangedComponents.groovy)

### executable

Ensures that the given executable is available on the current Jenkins agent, installing it with a Jenkins tool installer 
if necessary.

### kustomize

Returns the customized configuration per contents of a kustomization.yaml as a string.

### openshiftApply

Requests OpenShift to apply the given configuration and waits for the rollout to be finished.

### openshiftDiff

Compares the OpenShift configuration/resources given in the <b>configuration</b> argument against the resources in an
OpenShift project.

### openshiftProcess

Processes an OpenShift template to get the final configuration. Use the output with **openshiftApply** to apply the configuration against your project.

### openshiftStartBuild

Requests OpenShift to start build from the specified build config and waits for the build to be finished.

### owaspDependencyCheck

Runs the OWASP dependency-check tool.

### replaceFromVault

Substitutes variables in a provided String with secrets retrieved from HashiCorp Vault.

### trackComponentVersions

This function tracks information about the current deployment state of a component on a specific environment.
See it's [documentation](vars/trackComponentVersions.txt)

Source: [vars/trackComponentVersions.groovy](vars/trackComponentVersions.groovy)

### withGitCrypt

Unlocks the Git repository in the current directory with <code>git-crypt</code> for the duration of the scope of the
 step and locks it again afterwards.

## Groovy source files

### DockerHub

This class was implemented to read and delete Docker Images by tag names.

File: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

## Testing

### Unit tests

Tests can be executed with `./gradlew clean test`

* Documentation for the spock test framework can be found here: <https://spockframework.org/>
* Documentation for the Jenkins test framework can be found here: <https://github.com/ExpediaGroup/jenkins-spock>
  * [JenkinsPipelineSpecification](https://www.javadoc.io/doc/com.homeaway.devtools.jenkins/jenkins-spock/2.0.1/com/homeaway/devtools/jenkins/testing/JenkinsPipelineSpecification.html)
* Source: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

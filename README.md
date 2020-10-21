# jenkins-pipeline-shared-libraries

Shared functionality for Jenkins Pipeline Groovy scripts.

Included by the [Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin)

Documentation: <https://jenkins.io/doc/book/pipeline/shared-libraries/>

## global variables

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

### openshiftStartBuild

Requests OpenShift to start build from the specified build config and waits for the build to be finished.

### owaspDependencyCheck

Runs the OWASP dependency-check tool.

### replaceFromVault

Substitutes variables in a provided String with secrets retrieved from HashiCorp Vault.

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

Documentation for the test framework can be found here: <https://github.com/ExpediaGroup/jenkins-spock>
Source: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

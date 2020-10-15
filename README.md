# jenkins-pipeline-shared-libraries

Shared functionality for Jenkins Pipeline Groovy scripts.

Included by the [Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin)

Documentation: <https://jenkins.io/doc/book/pipeline/shared-libraries/>

## global variables

### kustomize

Custom step to execute <code>kustomize</code>. See it's [documentation](vars/kustomize.txt)

Source: [vars/kustomize.groovy](vars/kustomize.groovy)

### openshiftApply

Requests OpenShift to apply the given configuration and waits for the rollout to be finished. See it's
[documentation](vars/openshiftApply.txt)

Source: [vars/openshiftApply.groovy](vars/openshiftApply.groovy)

### openshiftDiff

Compares the OpenShift configuration/resources given in the <b>configuration</b> argument against the resources in an
 OpenShift project. [documentation](vars/openshiftDiff.txt)

Source: [vars/openshiftDiff.groovy](vars/openshiftDiff.groovy)

### openshiftStartBuild

Requests OpenShift to start build from the specified build config and waits for the build to be finished. See it's
[documentation](vars/openshiftStartBuild.txt)

Source: [vars/openshiftStartBuild.groovy](vars/openshiftStartBuild.groovy)

### openshiftUtils

Functionality to interact with an OpenShift cluster. See it's [documentation](vars/openshiftUtils.txt)

Source: [vars/openshiftUtils.groovy](vars/openshiftUtils.groovy)

### owaspDependencyCheck

Custom step to execute the OWASP Dependency Check. See it's [documentation](vars/owaspDependencyCheck.txt)

Source: [vars/owaspDependencyCheck.groovy](vars/owaspDependencyCheck.groovy)

### replaceFromVault

Substitutes variables in a provided String with secrets retrieved from HashiCorp Vault. 
See it's [documentation](vars/replaceFromVault.txt)

Source: [vars/replaceFromVault.groovy](vars/replaceFromVault.groovy)

### withGitCrypt

Unlocks the Git repository in the current directory with <code>git-crypt</code> for the duration of the scope of the
 step and locks it again afterwards. See it's [documentation](vars/withGitCrypt.txt)

Source: [vars/withGitCrypt.groovy](vars/withGitCrypt.groovy)


## Groovy source files

### DockerHub

This class was implemented to read and delete Docker Images by tag names.

File: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

## Testing

### Unit tests

Tests can be executed with `./gradlew clean test`

Documentation for the test framework can be found here: <https://github.com/ExpediaGroup/jenkins-spock>
Source: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

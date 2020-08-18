# jenkins-pipeline-shared-libraries

Shared functionality for Jenkins Pipeline Groovy scripts.

Included by the [Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin)

Documentation: <https://jenkins.io/doc/book/pipeline/shared-libraries/>

## global variables

### openshiftUtils

Functionality to interact with an OpenShift cluster. See it's [documentation](vars/openshiftUtils.txt)

File: [vars/openshiftUtils.groovy](vars/openshiftUtils.groovy)

### owaspDependencyCheck

Custom step to execute the OWASP Dependency Check. See it's [documentation](vars/owaspDependencyCheck.txt)

File: [vars/owaspDependencyCheck.groovy](vars/owaspDependencyCheck.groovy)

## Groovy source files

### DockerHub

This class was implemented to read and delete Docker Images by tag names.

File: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

## Testing

### Unit tests

Tests can be executed with `$ ./gradlew clean test`  
Documentation for the Testframework can be found here: [https://github.com/ExpediaGroup/jenkins-spock](https://github.com/ExpediaGroup/jenkins-spock)
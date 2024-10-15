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

Compares the OpenShift configuration/resources given in the **configuration** argument against the resources in an
OpenShift project.

### openshiftProcess

Processes an OpenShift template to get the final configuration. Use the output with **openshiftApply** to apply the configuration against your project.

### openshiftStartBuild

Requests OpenShift to start build from the specified build config and waits for the build to be finished.

### owaspDependencyCheck

Runs the OWASP dependency-check tool.

### rocketSend

Sends message to the specified WebHook.

## Groovy source files

### DockerHub

This class was implemented to read and delete Docker Images by tag names.

File: [DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

### Quay

This class was implemented to organize images by API on the Quay registry.

File: [Quay.groovy](src/com/puzzleitc/jenkins/Quay.groovy)

Sample pipeline:

```Groovy
import com.puzzleitc.jenkins.Quay

pipeline {
    agent any
    environment {
        CRED = 'quay-token'
        REGISTRY_URL = 'https://quay.io'
        ORG = 'test-org'
        REPOSITORY = 'test-repository'
        TAG='latest'
        NEW_TAG='later'
    }
    stages {
        stage('set tag') {
            steps {
                script {
                    Quay quay = new Quay(this, env.CRED, env.REGISTRY_URL)

                    // get sha of image referenced by the tag
                    def sha = quay.getTagManifest(env.ORG, env.REPOSITORY, env.TAG)
                    println "SHA: " + sha

                    // add new tag to image
                    quay.addTag(env.ORG, env.REPOSITORY, sha, env.NEW_TAG)
                }
            }
        }
    }
}
```

## Workflows

The [Integration Pipeline](doc/Integration-Pipeline.md) provides functionality that helps with microservice promotion and deployment.

## Testing

See the [testing concept](doc/testing.md)

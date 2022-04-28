# jenkins-pipeline-shared-libraries

Shared functionality for Jenkins Pipeline Groovy scripts.

Included by the [Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin)

Documentation: <https://jenkins.io/doc/book/pipeline/shared-libraries/>

## global variables

### addDeployLinks

Adds a link(to another job) for a specific build from the build history of a jenkins job.
See it's [documentation](vars/addDeployLinks.txt).
This function is a component of the [Integration Pipeline](doc/Integration-Pipeline.md).

Source: [vars/addDeployLinks.groovy](vars/addDeployLinks.groovy)

### cleanBuilds

Cleans up the build history of the jenkins job. It can differentiate deployments to specific environments.
See it's [documentation](vars/cleanBuilds.txt).
This function is a component of the [Integration Pipeline](doc/Integration-Pipeline.md).

Source: [vars/cleanBuilds.groovy](vars/cleanBuilds.groovy)

### deployChangedComponents

This function can be used as a template for deploying a component on a specific environment.
There is no actual implementation of a deployment but a placeholder with an echo output.
See it's [documentation](vars/deployChangedComponents.txt).
This function is a component of the [Integration Pipeline](doc/Integration-Pipeline.md).

Source: [vars/deployChangedComponents.groovy](vars/deployChangedComponents.groovy)

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

### replaceFromVault

Substitutes variables in a provided String with secrets retrieved from HashiCorp Vault.

### trackComponentVersions

This function tracks information about the current deployment state of a component on a specific environment.
See it's [documentation](vars/trackComponentVersions.txt).
This function is a component of the [Integration Pipeline](doc/Integration-Pipeline.md).

Source: [vars/trackComponentVersions.groovy](vars/trackComponentVersions.groovy)

### withGitCrypt

Unlocks the Git repository in the current directory with `git-crypt` for the duration of the scope of the
 step and locks it again afterwards.

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

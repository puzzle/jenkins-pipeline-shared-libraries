# Shared Library testing

Concept for how to test the functionality of this shared library.

## testing

There are different test types used for testing at different levels.

### unit tests

Groovy functions and classes are tested with unit tests.
They are located in [test/groovy](../test/groovy)

Function unit testing is done with [BasePipelineTest's](https://github.com/jenkinsci/JenkinsPipelineUnit/blob/master/src/main/groovy/com/lesfurets/jenkins/unit/BasePipelineTest.groovy)
from [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit)

Eg. [test/groovy/vars](../test/groovy/vars) for scripts in [vars](../vars)

Whole scripts are tested with a pipeline file. Mostly green-path testing is enough. Edge cases are tested in spec classes.

Eg. [test/groovy/vars/pipeline](../test/groovy/vars/pipeline) for scripts in [vars](../vars)

## test execution

Tests can be executed with `./gradlew clean test`

## further documentation

* [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit)
* Documentation for the spock test framework can be found here: <https://spockframework.org/>
* Documentation for the Jenkins test framework can be found here: <https://github.com/ExpediaGroup/jenkins-spock>
  * [JenkinsPipelineSpecification](https://www.javadoc.io/doc/com.homeaway.devtools.jenkins/jenkins-spock/2.0.1/com/homeaway/devtools/jenkins/testing/JenkinsPipelineSpecification.html)
* Source: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

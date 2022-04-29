# Shared Library testing

Concept for how to test the functionality of this shared library.

## testing

There are different test types used for testing at different levels.

* unit tests (JUnit 4)
  * single component
  * internal functions of the library
* unit tests ([JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit))
  * exposed library functionality (vars directory and classes)
* integration tests ([Jenkins Unit Test Harness](https://github.com/jenkinsci/jenkins-test-harness))
  * several components working together
  * functionality using Jenkins dependencies only
  * functionality using internal dependencies only
* end-to-end tests (on real jenkins)
  * external services needed (K8S, Registries, ...)
  * complex test setup

INFO: Usage of the [spock test framework](https://spockframework.org/) is deprecated.

### unit tests

Groovy functions and classes are tested with unit tests. Dependencies are mocked.
They are located in [test/groovy](../test/groovy)

Function unit testing is done with [BasePipelineTest's](https://github.com/jenkinsci/JenkinsPipelineUnit/blob/master/src/main/groovy/com/lesfurets/jenkins/unit/BasePipelineTest.groovy)
from [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit). The test files are named *Test.groovy

Eg. [test/groovy/vars](../test/groovy/vars) for scripts in [vars](../vars)

Whole scripts are tested with a pipeline file. Mostly green-path testing is enough.
Edge cases are tested in spec classes. The test files are named *PipelineTest.groovy

## test execution

Tests can be executed with `./gradlew clean test`

## further documentation

* [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit)
* Documentation for the Jenkins test framework can be found here: <https://github.com/ExpediaGroup/jenkins-spock>
  * [JenkinsPipelineSpecification](https://www.javadoc.io/doc/com.homeaway.devtools.jenkins/jenkins-spock/2.0.1/com/homeaway/devtools/jenkins/testing/JenkinsPipelineSpecification.html)
* Source: [src/com/puzzleitc/jenkins/DockerHub.groovy](src/com/puzzleitc/jenkins/DockerHub.groovy)

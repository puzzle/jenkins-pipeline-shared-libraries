package resources.vars.check

pipeline {
    agent any
    stages {
        stage("Test") {
            check.mandatoryParameter("hallo")
        }
    }
}
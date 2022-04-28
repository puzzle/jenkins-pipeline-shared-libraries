pipeline {
    agent any
    parameters {
        string(name: 'hallo', defaultValue: 'Mr Jenkins')
    }
    stages {
        stage("Test") {
            check.mandatoryParameter("hallo")
        }
    }
}
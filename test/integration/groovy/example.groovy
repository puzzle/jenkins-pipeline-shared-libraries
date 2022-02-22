pipeline {
    agent none
    stages {
        stage("Test") {
            steps {
                script {
                    openshiftStartBuild(
                            buildConfigName: 'my-app',
                            cluster: 'OpenShiftCloudscaleProduction',
                            project: 'my-project',
                            fromDir: 'target/image')
                }
            }
        }
    }
}
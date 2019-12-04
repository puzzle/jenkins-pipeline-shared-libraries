def call(String extraArgs = "") {
    String result = currentBuild.result?.toLowerCase() ?: 'success'
    node {
        withEnv(["PATH+DC=${tool name: '5.2.4', type: 'dependency-check'}/bin"]) {                    
            sh 'mkdir -p data report'
            // sh 'chmod 777 data report'
            sh "dependency-check.sh --scan app --scan api --format 'ALL' --project 'OWASP Dependency Check' --out report ${extraArgs}"
        }
        dependencyCheckPublisher pattern: 'report/dependency-check-report.xml'
    }
}

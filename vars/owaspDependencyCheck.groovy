def callString(Map args = "", String... scanDirs) {
    String result = currentBuild.result?.toLowerCase() ?: 'success'
    withEnv(["PATH+DC=${tool name: '5.2.4', type: 'dependency-check'}/bin"]) {                    
        sh 'mkdir -p data report'
        // sh 'chmod 777 data report'
        scanArgs = scanDirs.collect { "--scan '$it'"}.join(' ')
        sh "dependency-check.sh ${scanArgs} --format 'ALL' --project 'OWASP Dependency Check' --out report ${args.extraArgs}"
    }
    dependencyCheckPublisher pattern: 'report/dependency-check-report.xml'
}

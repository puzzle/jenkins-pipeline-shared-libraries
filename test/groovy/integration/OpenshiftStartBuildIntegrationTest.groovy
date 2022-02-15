package groovy.integration

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule

class OpenshiftStartBuildIntegrationTest {

    @Rule public JenkinsRule jenkins = new JenkinsRule()

    @Before
    public void configureSharedLibrary(){
        jenkins.timeout = 30
        final LibraryRetriever retriever = new LocalLibraryRetriever()
        final LibraryConfiguration  localLibrary = new LibraryConfiguration("jenkins-shared-library", retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'master'
        localLibrary.allowVersionOverride = false

        GlobalLibraries.get().libraries = [localLibrary]
    }

    @Test
    public void testStartBuild() {
        final WorkflowJob job = jenkins.createProject(WorkflowJob.class, "project")
        job.setDefinition(new CpsFlowDefinition("""
pipeline {
    agent any
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
""", false))

        final jobRun = job.scheduleBuild2(0)

        jenkins.assertBuildStatusSuccess(jobRun)
    }

}

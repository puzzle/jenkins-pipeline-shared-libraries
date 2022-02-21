package groovy.integration


import jenkins.model.Jenkins
import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.MockAuthorizationStrategy

class OpenshiftStartBuildIntegrationTest {

    @Rule public JenkinsRule jenkins = new JenkinsRule()

    @Before
    public void configureSharedLibrary(){
        jenkins.timeout = 30
        final LibraryRetriever retriever = new LocalLibraryRetriever()
        final LibraryConfiguration  localLibrary = new LibraryConfiguration("jenkins-shared-library", retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'main'
        localLibrary.allowVersionOverride = false

        GlobalLibraries.get().libraries = [localLibrary]

        jenkins.jenkins.setSecurityRealm(jenkins.createDummySecurityRealm())
        jenkins.jenkins.setAuthorizationStrategy(new MockAuthorizationStrategy().
                grant(Jenkins.ADMINISTER).everywhere().to("admin")
        );
    }

    @Test
    public void testStartBuild() {

        def script = new File('test/groovy/integration/example.groovy')

        final WorkflowJob job = jenkins.createProject(WorkflowJob, "project")
        job.setDefinition(new CpsFlowDefinition(script.text, false))

        ScriptApproval.get().preapproveAll()

        final jobRun = job.scheduleBuild2(0)

        jobRun.get();
        jenkins.interactiveBreak();
        jenkins.assertBuildStatusSuccess(jobRun)
    }

}

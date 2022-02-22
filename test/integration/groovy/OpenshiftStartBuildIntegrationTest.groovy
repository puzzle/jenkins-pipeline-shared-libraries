import com.mkobit.jenkins.pipelines.codegen.LocalLibraryRetriever
import hudson.model.Result
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
        def script = new File('test/integration/groovy/example.groovy')

        final WorkflowJob job = jenkins.createProject(WorkflowJob, "project")
        job.setDefinition(new CpsFlowDefinition(script.text, false))

        ScriptApproval.get().preapproveAll()

        //final jobRun = job.scheduleBuild2(0)
        jenkins.buildAndAssertStatus(Result.FAILURE, job)
        jenkins.assertLogContains("CredentialNotFoundException: my-project-cicd-deployer", job.getBuilds()[0])
    }

}

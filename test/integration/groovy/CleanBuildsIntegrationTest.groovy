import com.jenkinsci.plugins.badge.action.BadgeAction
import com.mkobit.jenkins.pipelines.codegen.LocalLibraryRetriever
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import static org.junit.Assert.*;

class CleanBuildsIntegrationTest {

    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Before
    public void configureSharedLibrary(){
        final LibraryRetriever retriever = new LocalLibraryRetriever()
        final LibraryConfiguration localLibrary = new LibraryConfiguration("jenkins-shared-library", retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'main'
        localLibrary.allowVersionOverride = false

        GlobalLibraries.get().libraries = [localLibrary]

        /*jenkins.jenkins.setSecurityRealm(jenkins.createDummySecurityRealm())
        jenkins.jenkins.setAuthorizationStrategy(new MockAuthorizationStrategy().
                grant(Jenkins.ADMINISTER).everywhere().to("admin")
        );*/
    }


    @Test
    public void "even with no builds should succeed"() {
        rule.createProject(WorkflowJob, "project-1")

        def test = rule.createProject(WorkflowJob, "test")
        test.definition = new CpsFlowDefinition("""
pipeline {
    agent any
    stages {
        stage("Test") {
            steps {
                cleanBuilds job: 'project-1', maxKeepBuilds: 1
            }
        }
    }
}
""", false)

        def testRun = test.scheduleBuild2(0);

        rule.waitForCompletion(testRun.get())
        rule.assertBuildStatusSuccess(testRun.get())
    }

    @Test
    public void "clean one build"() {
        def project1 = rule.createProject(WorkflowJob, "project-1")
        project1.definition = new CpsFlowDefinition("""
node {
    echo "Hello world!"
}
""", false)

        def badgeAction = new BadgeAction(BadgeAction.getIconPath("completed.gif"), "deployed")

        // Schedule 2 builds
        rule.waitForCompletion(project1.scheduleBuild2(0, badgeAction).get())
        rule.waitForCompletion(project1.scheduleBuild2(0, badgeAction).get())


        for (build in project1.getBuilds().collect()) {
            build.keepLog()
        }

        def test = rule.createProject(WorkflowJob, "test")
        test.definition = new CpsFlowDefinition("""
pipeline {
    agent any
    stages {
        stage("Test") {
            steps {
                cleanBuilds job: 'project-1', maxKeepBuilds: 1
            }
        }
    }
}
""", false)

        rule.buildAndAssertSuccess(test)

        // interactive break
        // rule.interactiveBreak()

        rule.assertLogContains("Deleting build project-1 #1", test.lastBuild)

        def builds = project1.getBuilds().collect()
        assertEquals(1, builds.size())
        assertEquals(2, builds.first().number) // latest build should be kept
        assertNull(builds.first().previousBuild)
    }

}

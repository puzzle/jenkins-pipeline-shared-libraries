package groovy.integration

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule

class SimpleTest {

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test
    void "echo should work"() throws Exception {
        def project = j.createProject(WorkflowJob.class)
        project.setDefinition(new CpsFlowDefinition("""
pipeline {
    agent any
    stages {
        stage("Hello") {
            steps {
                echo "Hello world!"
            }
        }
    }
}
""", true))

        def build = project.scheduleBuild2(0)
        j.assertBuildStatusSuccess(build)
        j.assertLogContains("Hello world!", build.get())
    }

}

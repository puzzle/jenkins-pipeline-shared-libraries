package groovy.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue;

class KustomizeSpec extends BasePipelineTest {

    Script kustomize;

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()

        helper.registerAllowedMethod("ansiColor", [String.class, Closure.class])
        helper.registerAllowedMethod("executable", [Map.class], { "/path/bin" })

        kustomize = loadScript("vars/kustomize.groovy")
    }

    @Test
    void "it should call kustomize shell command"() {
        kustomize.call("openshift/overlays/dev")
        assertTrue helper.callStack.stream()
                .filter { it.methodName == "sh"}
                .map {it.args["script"] as String}
                .filter { it != null }
                .any {
                    it.contains("/path/bin/kustomize build openshift/overlays/dev")
                }
    }

    @Test(expected = IllegalArgumentException.class)
    void "it should fail when path is not set"() {
        kustomize.call([:])
    }

}
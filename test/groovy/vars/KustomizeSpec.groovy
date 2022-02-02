package groovy.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.assertj.core.api.Assertions.*;

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
        assertThat(helper.callStack
                .findAll { it.methodName == "sh" }
                .any { callArgsToString(it).contains("/path/bin/kustomize build openshift/overlays/dev") }
        ).isTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "it should fail when path is not set"() {
        kustomize.call([:])
    }

}
package groovy.vars

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.assertj.core.api.Assertions.*

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

class KustomizeTest extends BasePipelineTest {

    Script kustomize

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()

        helper.registerAllowedMethod("ansiColor", [String.class, Closure.class])
        helper.registerAllowedMethod("executable", [Map.class], { "/path/bin" })

        kustomize = loadScript("vars/kustomize.groovy")
    }

    @Test
    void itShouldCallKustomizeShellCommand() {
        // when
        kustomize.call("openshift/overlays/dev")

        // then
        assertThat(helper.callStack
                .findAll { it.methodName == "sh" }
                .any { callArgsToString(it).contains("/path/bin/kustomize build openshift/overlays/dev") }
        ).isTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void itShouldFailWhenPathIsNotSet() {
        // when
        kustomize.call([:])

        // then - expect IllegalArgumentException
    }

}
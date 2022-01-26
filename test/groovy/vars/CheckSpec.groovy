package groovy.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.assertj.core.api.Assertions.*;

class CheckSpec extends BasePipelineTest {

    private Script check

    @Override
    @Before
    void setUp() throws Exception {
        super.setUp()
        check = loadScript("vars/check.groovy")
    }

    @Test
    void "It aborts the pipeline"() throws Exception {
        // Override error behaviour as it is not correctly implemented by the test framework
        helper.registerAllowedMethod("error", [String.class])

        check.binding.setVariable("params", [:])

        check.mandatoryParameter("hallo");

        assertJobStatusAborted()

        assertThat(helper.callStack.stream()
                .filter({ it.methodName == "error" })
                .flatMap({ Arrays.stream(it.args) })
                .any({ it == "missing parameter: hallo" })).isTrue()
    }

    @Test
    void 'It doesn\'t abort the pipline'() {
        check.binding.setVariable('currentBuild', [result: 'null'])
        check.binding.setVariable('params', [hallo: 'test'])
        check.mandatoryParameter('hallo')

        assertThat(check.binding.getVariable("currentBuild").result as String)
                .isEqualTo("null")
        assertThat(helper.callStack).filteredOn({ it.methodName == "error" }).isEmpty()
    }
}
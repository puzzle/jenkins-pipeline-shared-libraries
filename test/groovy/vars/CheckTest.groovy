package groovy.vars

import static org.assertj.core.api.Assertions.*
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for vars/check.groovy
 */
class CheckTest extends BasePipelineTest {

    private Script check

    @Override
    @Before
    void setUp() {
        super.setUp()
        check = loadScript('vars/check.groovy')
    }

    @Test
    void itShouldAbortForMissingMandatoryParameter() {
        // given
        // Override error behaviour as it is not correctly implemented by the test framework
        helper.registerAllowedMethod('error', [String.class])
        check.binding.setVariable('params', [:])

        // when
        check.mandatoryParameter('hallo')

        // then
        assertJobStatusAborted()
        assertThat(helper.callStack.findAll { call ->
            call.methodName == 'error'
        }.any { call ->
            callArgsToString(call).contains('missing parameter: hallo')
        }).isTrue()
    }

    @Test
    void itShouldNotAbortForAvailableMandatoryParameter() {
        // given
        check.binding.setVariable('currentBuild', [result: 'null'])
        check.binding.setVariable('params', [hallo: 'test'])

        // when
        check.mandatoryParameter('hallo')

        // then
        assertThat(check.binding.getVariable('currentBuild').result as String).isEqualTo('null')
        assertThat(helper.callStack).filteredOn({ it.methodName == 'error' }).isEmpty()
    }
}

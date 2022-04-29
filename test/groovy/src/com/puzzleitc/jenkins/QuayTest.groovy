package groovy.src.com.puzzleitc.jenkins

import com.puzzleitc.jenkins.Quay

import static org.assertj.core.api.Assertions.*

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for vars/check.groovy
 */
class QuayTest extends BasePipelineTest {

    Script script
    Quay quay

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()

        quay = new Quay(script, 'myCred', 'myRegistryUrl')
    }

    @Test(expected = IllegalArgumentException.class)
    void itShouldFailForMissingMandatoryParameter() {
        // when
        new Quay(script, '')

        // then - expect IllegalArgumentException
    }

    @Test
    void itShouldUseDefaultRegistryUrl() {
        // when
        Quay myQuay = new Quay(script, 'cred')

        // then
        assertThat(myQuay.getRegistryUrl()).isEqualTo('https://quay.io')
    }

    @Test
    void itShouldGivenRegistryUrl() {
        // given
        String testUrl = 'reg.cool'

        // when
        Quay myQuay = new Quay(script, 'cred', testUrl)

        // then
        assertThat(myQuay.getRegistryUrl()).isEqualTo(testUrl)
    }

}

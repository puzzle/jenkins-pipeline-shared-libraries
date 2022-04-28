package groovy.pipeline

import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource
import static org.assertj.core.api.Assertions.assertThat

class CheckPipelineTest extends DeclarativePipelineTest {

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()
        helper.libLoader.preloadLibraryClasses = false
        def library = library().name('jenkins-shared-library')
                .defaultVersion('master')
                .allowOverride(true)
                .implicit(true)
                .targetPath('<notNeeded>')
                .retriever(projectSource())
                .build()
        helper.registerSharedLibrary(library)
    }

    @Test
    void "mandatory parameter not set"() {
        // when
        runScript('test/resources/vars/check/mandatoryParameterMissing.groovy')

        // then
        assertThat(helper.callStack.findAll { call ->
            call.methodName == "error"
        }.any { call ->
            callArgsToString(call).contains("missing parameter: hallo")
        }).isTrue()
        printCallStack()
    }

    @Test
    void "mandatory parameter set by default parameter"() {
        // when
        runScript('test/resources/vars/check/mandatoryParameter.groovy')

        // then
        assertJobStatusSuccess()
    }
}

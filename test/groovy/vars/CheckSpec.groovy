package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class CheckSpec extends JenkinsPipelineSpecification {

    def check = loadPipelineScriptForTest('vars/check.groovy')

    def 'It aborts the pipline' () {
        setup:
            check.getBinding().setVariable('currentBuild', [:])
        when:
            check.mandatoryParameter('hallo')
        then:
            check.getBinding().getVariable('currentBuild').result == 'ABORTED'
            1 * getPipelineMock("error").call('missing parameter: hallo')
    }

    def 'It doesn\'t abort the pipline' () {
        setup:
            check.getBinding().setVariable('currentBuild', [result: 'null'])
            check.getBinding().setVariable('params', [hallo: 'test'])
        when:
            check.mandatoryParameter('hallo')
        then:
            check.getBinding().getVariable('currentBuild').result == 'null'
    }
}
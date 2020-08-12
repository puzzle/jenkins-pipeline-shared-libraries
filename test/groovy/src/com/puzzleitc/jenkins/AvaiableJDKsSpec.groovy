package groovy.src.com.puzzleitc.jenkins

import groovy.mock.interceptor.MockFor
import spock.lang.*
import com.puzzleitc.jenkins.AvailableJDKs

class AvaiableJDKsSpec extends Specification {

    def 'it reads all available jdk versions' () {
        setup:
            def text = new File('test/resources/JDKInstaller.json').text
            def mockFile = new MockFor(File)
            mockFile.demand.getText{ text }
        mockFile.use {
            when:
                def availableJdks = AvailableJDKs.jdks()
            then:
                assert availableJdks.size() == 2
                assert availableJdks.get('JDK8').version == '8u192'
                assert availableJdks.get('JDK9').version == '9.0.4'
                assert !availableJdks.containsKey('JDK7')
        }
    }
}

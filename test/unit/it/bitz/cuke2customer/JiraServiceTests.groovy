package it.bitz.cuke2customer

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.mock.interceptor.MockFor
import groovyx.net.http.HTTPBuilder
import it.bitz.cuke2customer.model.Feature
import org.junit.Test

@TestMixin (GrailsUnitTestMixin)
class JiraServiceTests {

    @Test
    void shouldNotPropagateExceptions () {
        def mock = new MockFor(HTTPBuilder)
        mock.demand.setHeaders (1) {Map m -> }
        mock.demand.post (1) {Map m -> }
        mock.demand.get (1) {Map m ->
            throw new Exception("This exception should not be propagated")
        }
        mock.use {
            assertEquals([], new JiraService().fetchCommentsForFeature(new Feature(jiraId: 'test-123')))
        }
    }

}

package it.bitz.cuke2customer

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FeatureService)
class FeatureServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "loads features in directory"() {
        given: 'a directory structure with feature files'
        String featureDir = 'test/resources/FeatureService/FeatureFiles/'

        when: 'loading features in that directory structure'
        service.loadFeatures(featureDir)

        then: "the FeatureService should return the feature files's contents for their hash codes"
        assertServiceLoadedFile(featureDir + 'dir1/dir1.1/Feature1.1.1.feature')
        assertServiceLoadedFile(featureDir + 'dir1/dir1.1/Feature1.1.2.feature')
        assertServiceLoadedFile(featureDir + 'dir1/Feature1.1.feature')
        assertServiceLoadedFile(featureDir + 'dir2/Feature2.1.feature')
        assertServiceLoadedFile(featureDir + 'Feature0.1.feature')
    }

    private void assertServiceLoadedFile(String file) {
        String fileContent = new File(file).text
        assert fileContent == service.getFeatureWithHashCode(fileContent.hashCode().toString())
    }

    void "finds features using one search term"() {
        given: 'two features containing the term "term1"'
        String featureDir = 'test/resources/FeatureService/SearchableFeatures/'
        service.loadFeatures(featureDir)

        when: 'searching for "term1"'
        List<String> result = service.search(['term1'])

        then: 'both features should be found'
        result.size() == 2
        result.contains(new File(featureDir + 'searchable1.feature').text)
        result.contains(new File(featureDir + 'searchable2.feature').text)
    }

    void "finds features using two search terms"() {
        given: 'two features containing the term "term1", one of which also containing the term "term2"'
        String featureDir = 'test/resources/FeatureService/SearchableFeatures/'
        service.loadFeatures(featureDir)

        when: 'searching for "term1" and "term2"'
        List<String> result = service.search(['term1', 'term2'])

        then: 'the features containing both search terms should be found'
        result.size() == 1
        result.contains(new File(featureDir + 'searchable1.feature').text)
    }

    void "searches case insensitive"() {
        given: 'two features containing the term "term1"'
        String featureDir = 'test/resources/FeatureService/SearchableFeatures/'
        service.loadFeatures(featureDir)

        when: 'searching for "TERM1"'
        List<String> result = service.search(['TERM1'])

        then: 'both features should be found'
        result.size() == 2
        result.contains(new File(featureDir + 'searchable1.feature').text)
        result.contains(new File(featureDir + 'searchable2.feature').text)
    }

    void "finds no features for empty search terms"() {
        given: 'two features "'
        String featureDir = 'test/resources/FeatureService/SearchableFeatures/'
        service.loadFeatures(featureDir)

        when: 'searching for with an empty string an search term'
        List<String> result = service.search([''])

        then: 'no features should be found'
        result.size() == 0
    }
}

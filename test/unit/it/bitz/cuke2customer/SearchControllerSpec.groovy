package it.bitz.cuke2customer

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SearchController)
class SearchControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "a search resulting in one found feature should redirect to show that feature"() {
        when: 'only one feature is found when searching'
        String foundFeature = 'one feature'
        controller.featureService = [
              search: { List<String> searchTerms ->
                  return [foundFeature]
              }
        ] as FeatureService
        controller.search('term')

        then: 'the controller should redirect to showing that feature'
        response.redirectUrl == "/feature/showFeature?featureHashCode=${foundFeature.hashCode()}"
    }

    void "a search resulting in more than one found feature should render the search results"() {
        when: 'only two features are found when searching'
        List<String> foundFeatures = ['feature 1', 'feature 2']
        controller.featureService = [
              search: { List<String> searchTerms ->
                  return foundFeatures
              }
        ] as FeatureService
        Map model = controller.search('term')

        then: 'the search results should be rendered'
        model.searchResults == foundFeatures
        model.query == 'term'
    }

    void "splits search query at spaces"() {
        List<String> splitSearchTerms = null
        controller.featureService = [
              search: { List<String> searchTerms ->
                  splitSearchTerms = searchTerms
                  return []
              }
        ] as FeatureService

        when: 'seaching with mutiple search terms separated by a space'
        controller.search('term1 term2  term3')

        then: 'each search term should be passed separately to the FeatureService'
        splitSearchTerms.size() == 3
        splitSearchTerms.containsAll(['term1', 'term2', 'term3'])
    }
}

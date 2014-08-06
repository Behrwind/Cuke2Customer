package it.bitz.cuke2customer

class SearchController {

    FeatureService featureService

    def search(String query) {
        List<String> searchTerms = splitSearchTerms(query)
        List<String> results = featureService.search(searchTerms)
        if (results.size() == 1) {
            redirect(controller: 'feature', action: 'showFeature', params: [featureHashCode: results.first().hashCode()])
        }
        return [query: query, searchResults: results]
    }

    private List<String> splitSearchTerms (String query) {
        if (!query.contains(' ')) {
            return [query]
        }
        return query.split(' ').findAll { it }      // findAll { it } removes empty strings
    }
}

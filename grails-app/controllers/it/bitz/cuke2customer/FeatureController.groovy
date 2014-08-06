package it.bitz.cuke2customer

import it.bitz.cuke2customer.model.Comment
import it.bitz.cuke2customer.model.Feature
import it.bitz.cuke2customer.parser.GherkinParser
import org.apache.commons.io.FileUtils

class FeatureController {

    def grailsApplication

    SvnAdapterService svnAdapterService
    FeatureService featureService
    JiraService jiraService

    def index () {
        redirect (action: 'listFeatures')
    }

    def listFeatures () {
    }

    def show () {
        redirect (action: 'showFeature', params: params)
    }

    def showFeature (String featureHashCode) {
        String gherkin = featureService.getFeatureWithHashCode(featureHashCode)
        Feature feature = new GherkinParser(useJira()).parse(gherkin)
        List<Comment> jiraComments = fetchJiraComments(feature)
        return [feature: feature, comments: jiraComments]
    }

    private fetchJiraComments(Feature feature) {
        if (useJira()) {
            return jiraService.fetchCommentsForFeature(feature).sort { it.date }.reverse()
        }
        return []
    }

    private boolean useJira () {
        return grailsApplication.config.project.jira.integration.toUpperCase() == 'ON'
    }


    def retrieveFeatures () {
        final String dir = grailsApplication.config.project.feature.directory
        if (useSvn()) {
            FileUtils.forceDelete(new File(dir))
            svnAdapterService.checkoutLatestRevision (dir)
        }
        featureService.loadFeatures(dir)
        redirect(action: 'listFeatures')
    }

    private boolean useSvn () {
        return grailsApplication.config.project.svn.integration.toUpperCase() == 'ON'
    }
}

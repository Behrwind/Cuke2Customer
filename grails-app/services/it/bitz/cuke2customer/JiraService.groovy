package it.bitz.cuke2customer

import groovyx.net.http.HTTPBuilder
import it.bitz.cuke2customer.model.Comment
import it.bitz.cuke2customer.model.Feature

import java.text.SimpleDateFormat

class JiraService {

    String jiraUrl
    String jiraUser
    String jiraPassword

    List<Comment> fetchCommentsForFeature (Feature feature) {
        if (!feature?.jiraId) return []
        Object jiraIssue = fetchIssueFromJira(feature)
        return parseComments(jiraIssue)
    }

    private List<Comment> parseComments(jiraIssue) {
        jiraIssue?.fields?.comment?.comments?.collect { comment ->
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            new Comment(author: comment.author.displayName, body: comment.body, date: dateFormat.parse(comment.updated))
        } ?: []
    }

    private Object fetchIssueFromJira(Feature feature) {
        try {
            def http = new HTTPBuilder(jiraUrl)
            http.setHeaders('Content-Type': 'application/json;')
            http.post(path: '/rest/auth/1/session', body: '{"username": "' + jiraUser + '", "password": "' + jiraPassword + '"}')
            String jiraPrefix = grails.util.Holders.grailsApplication.config.project.jiraPrefix
            return http.get(path: "/rest/api/latest/issue/$jiraPrefix$feature.jiraId")
        } catch (Exception e) {
            log.error("unable to fetch JIRA issue for ${feature?.jiraId}", e)
            return null
        }
    }

}

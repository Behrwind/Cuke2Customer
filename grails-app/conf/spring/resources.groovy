import grails.util.Holders
import it.bitz.cuke2customer.JiraService
import it.bitz.cuke2customer.SvnAdapterService

// Place your Spring DSL code here
beans = {

    def grailsApplication = Holders.grailsApplication

    if(grailsApplication.config.project.svn.integration.toUpperCase() == 'ON') {
        versionControlAdapter(SvnAdapterService) {
            svnUser = grailsApplication.config.project.svnUser
            svnPassword = grailsApplication.config.project.svnPassword
            svnUrl = grailsApplication.config.project.svnUrl
        }
    }

    if(grailsApplication.config.project.jira.integration.toUpperCase() == 'ON') {
        jiraService(JiraService) {
            jiraUser = grailsApplication.config.project.jiraUser
            jiraPassword = grailsApplication.config.project.jiraPassword
            jiraUrl = grailsApplication.config.project.jiraUrl
        }
    }


}

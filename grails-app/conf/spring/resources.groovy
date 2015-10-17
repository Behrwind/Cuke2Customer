import grails.util.Holders
import it.bitz.cuke2customer.GitAdapterService
import it.bitz.cuke2customer.JiraService
import it.bitz.cuke2customer.SvnAdapterService
import it.bitz.cuke2customer.enums.VCSIntegration

// Place your Spring DSL code here
beans = {

    def grailsApplication = Holders.grailsApplication

    VCSIntegration vcsIntegration

    try {
        vcsIntegration = VCSIntegration.valueOf (grailsApplication.config.project.vcs.integration)
    } catch (IllegalArgumentException e) {
        log.error ("${grailsApplication.config.project.vcs.integration} is not a valid option for project.vcs.integration, vcs integration will be set to OFF")
        vcsIntegration = VCSIntegration.OFF
    }

    switch (vcsIntegration) {
        case VCSIntegration.GIT:
            versionControlAdapter (GitAdapterService) {
                gitUser = grailsApplication.config.project.vcsUser
                gitPassword = grailsApplication.config.project.vcsPassword
                gitUrl = grailsApplication.config.project.vcsUrl
            }
            break
        case VCSIntegration.SVN:
            versionControlAdapter (SvnAdapterService) {
                svnUser = grailsApplication.config.project.vcsUser
                svnPassword = grailsApplication.config.project.vcsPassword
                svnUrl = grailsApplication.config.project.vcsUrl
            }
            break
        case VCSIntegration.OFF:
            break
        default:
            log.error ("the vcs integration option ${vcsIntegration.name ()} cannot be handled yet")
    }

    if (grailsApplication.config.project.jira.integration.toUpperCase () == 'ON') {
        jiraService (JiraService) {
            jiraUser = grailsApplication.config.project.jiraUser
            jiraPassword = grailsApplication.config.project.jiraPassword
            jiraUrl = grailsApplication.config.project.jiraUrl
        }
    }

}

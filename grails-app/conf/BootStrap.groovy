import it.bitz.cuke2customer.FeatureService
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

    GrailsApplication grailsApplication
    FeatureService featureService

    def init = { servletContext ->
        final String dir = grailsApplication.config.project.feature.directory
        featureService.loadFeatures(dir)
    }

    def destroy = {
    }

}

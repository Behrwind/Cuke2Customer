import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

grails.config.locations = ["classpath:project.properties"] // for project-specific settings in 'project.properties'

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
      all: '*/*',
      atom: 'application/atom+xml',
      css: 'text/css',
      csv: 'text/csv',
      form: 'application/x-www-form-urlencoded',
      html: ['text/html', 'application/xhtml+xml'],
      js: 'text/javascript',
      json: ['application/json', 'text/json'],
      multipartForm: 'multipart/form-data',
      rss: 'application/rss+xml',
      text: 'text/plain',
      xml: ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
        String port = System.getProperty('server.port') ?: '8080'
        grails.serverURL = "http://localhost:$port/$appName"
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = project.serverURL
    }
}

// log4j configuration
log4j = {
    info "grails.app"
    info "it.bitz"

    error 'org.codehaus.groovy.grails.web.servlet',        // controllers
          'org.codehaus.groovy.grails.web.pages',          // GSP
          'org.codehaus.groovy.grails.web.sitemesh',       // layouts
          'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
          'org.codehaus.groovy.grails.web.mapping',        // URL mapping
          'org.codehaus.groovy.grails.commons',            // core / classloading
          'org.codehaus.groovy.grails.plugins',            // plugins
          'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
          'org.springframework',
          'org.hibernate',
          'net.sf.ehcache.hibernate'


    appenders {
        appender new DailyRollingFileAppender (
              name: 'file',
              datePattern: "'.'yyyy-MM-dd",  // See the API for all patterns.
              fileName: "${System.getProperty('catalina.base') ?: 'target'}/logs/${config.project.shortName}-Features/${config.project.shortName}-Features.log",
              layout: pattern (conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
        )
    }
    root {
        info 'file', 'stdout'
        additivity = true
    }
}

if (!project.feature.directory) {
    project.feature.directory = System.getProperty ('java.io.tmpdir') + appName
}

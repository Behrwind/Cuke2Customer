<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${grailsApplication.config.project.shortName}-Features</title>
</head>
<body>
  <div>
      <g:link controller="feature" action="retrieveFeatures"><g:message code="reload.features"/></g:link>
  </div>
</body>
</html>
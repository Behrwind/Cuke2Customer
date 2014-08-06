<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${grailsApplication.config.project.shortName}-Features</title>
  <link type="text/css" href="${resource (dir: 'css', file: 'searchResults.css')}" rel="stylesheet"/>
</head>
<body>
  <div id="searchResults">
    <h1><g:message code="search.results.for"/> "${query}"</h1>
    <p>${searchResults.size()} <g:message code="search.results"/></p>

    <g:each in="${searchResults}" var="feature">
      <c2c:searchResult feature="${feature}"/>
    </g:each>
  </div>
</body>
</html>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${resource (dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource (dir: 'css', file: 'style.css')}" type="text/css">
    <g:layoutHead/>
  </head>

  <body>
    <a name="up" id="upAnchor"></a>

    <div id="header" role="banner">
      <h1>${grailsApplication.config.project.shortName} Features</h1>
      <g:render template="/layouts/search"/>
      <div style="clear: both;"></div>
    </div>
    <c2c:featureMenu/>
    <div class="content">
      <g:layoutBody/>
    </div>

    <div class="footer" role="contentinfo"></div>

    <div id="goUpLinkContainer"><a href="#up"><g:message code="go.up"/></a></div>
  </body>
</html>

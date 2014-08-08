<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <title>${feature.title}</title>
    <link type="text/css" href="${resource(dir: 'css', file: 'feature.css')}" rel="stylesheet"/>
</head>

<body>
<div class="featureHead">
    <div class="feature">
        <p class="title">
            ${feature.title}
            <g:if test="${jiraIntegration}">
                <span class="jiraId">
                    (<a href="${c2c.jiraUrl(feature: feature)}" target="_blank">${feature.jiraId}</a>)
                </span>
            </g:if>
        </p>
    </div>

    <g:if test="${jiraIntegration}">
        <c2c:jiraLinks feature="${feature}" comments="${comments}"/>
    </g:if>

    <c2c:description description="${feature.description}"/>
</div>


<div class="quickAccess lineOverScenarios" style="clear: both">
    <a href="#"><g:message code="scenarios.quick.access"/></a><br/>

    <div class="quickAccessLinks">
        <g:each in="${feature.scenarios}" var="scenario" status="index">
            <a href="#scenario-${index}">${index + 1}. ${scenario.title}</a>
            <c2c:tagMetadata taggable="${scenario}" short="${true}"/><br/>
        </g:each>
    </div>
</div>

<c2c:tagMetadataSymbolKey/>

<g:if test="${feature.background}">
    <div class="background indented">
        <p class="title">${feature.background.title}</p>
        <c2c:steps steps="${feature.background.steps}"/>
    </div>
</g:if>

<g:each in="${feature.scenarios}" var="scenario" status="scenarioIndex">
    <div class="scenario indented ${scenarioIndex % 2 == 1 ? 'odd' : ''}">
        <p class="title">
            <a name="scenario-${scenarioIndex}"></a>${scenarioIndex + 1}. ${scenario.title}

        <g:if test="${jiraIntegration}">
            <c2c:jiraUrlFromTags tags="${scenario.tags}"/>
        </g:if>
        <c2c:tagMetadata taggable="${scenario}"/>
        </p>

        <c2c:description description="${scenario.description}"/>

        <c2c:steps steps="${scenario.steps}"/>

        <g:if test="${scenario.outline}">
            <div class="examples">
                <p class="title">${scenario.examplesHeading}</p>
                <c2c:table table="${scenario.examplesTable}"/>
            </div>
        </g:if>
    </div>
</g:each>

<c2c:tagMetadataSymbolKey/>

<g:if test="${jiraIntegration}">
    <p class="commentsHeading">
        <a name="comments"><g:message code="comments"/></a>
    </p>

    <div id="commentsExplanation" class="jira indented">
        <p>
            <g:if test="${!comments.empty}">
                <g:message code="showing.jira.comments.for"/> ${feature.jiraId}.<br/>
                <a href="${c2c.jiraUrl(feature: feature)}" target="_blank"><g:message
                        code="join.jira.discussion"/></a>
            </g:if>
            <g:else>
                <g:message code="no.comments.for"/> ${feature.jiraId}.<br/>
                <a href="${c2c.jiraUrl(feature: feature)}" target="_blank">
                    <g:message code="create.comment.in.jira"/></a>
            </g:else>
        </p>
    </div>
    <g:if test="${comments.size() > 0}">
        <g:each in="${comments}" var="comment">
            <div class="comment indented">
                <p class="commentHead">
                    ${comment.author} <g:message code="on.date"/> <g:formatDate date="${comment.date}"
                                                                                format="${message(code: 'date.format.day')}"/>
                    <g:message code="at.time"/> <g:formatDate date="${comment.date}"
                                                              format="${message(code: 'date.format.time')}"/>
                </p>

                <p class="commentBody">${comment.body}</p>
            </div>
        </g:each>
    </g:if>
</g:if>

</body>
</html>

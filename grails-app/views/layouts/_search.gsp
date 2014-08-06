<div class="search">
  <g:form controller="search" action="search">
    <g:textField name="query" value="${query ?: ''}"/>
    <g:submitButton name="${message(code: 'search')}" value="${message(code: 'search')}"/>
  </g:form>
</div>
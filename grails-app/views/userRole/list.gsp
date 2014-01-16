
<%@ page import="vaers.UserRole" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'userRole.label', default: 'UserRole')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-userRole" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-userRole" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'userRole.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'userRole.lastUpdated.label', default: 'Last Updated')}" />
					
						<th><g:message code="userRole.role.label" default="Role" /></th>
					
						<th><g:message code="userRole.user.label" default="User" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${userRoleInstanceList}" status="i" var="userRoleInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${userRoleInstance.id}">${fieldValue(bean: userRoleInstance, field: "dateCreated")}</g:link></td>
					
						<td><g:formatDate date="${userRoleInstance.lastUpdated}" /></td>
					
						<td>${userRoleInstance.role.authority}</td>
					
						<td>${userRoleInstance.user.username}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${userRoleInstanceTotal}" />
			</div>
		</div>
	</body>
</html>

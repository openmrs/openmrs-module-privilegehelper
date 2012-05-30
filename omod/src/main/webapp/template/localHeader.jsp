<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/logger")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/privilegehelper/logger/log.form"><spring:message
				code="privilegehelper.log" /></a>
	</li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/assigner")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/privilegehelper/assigner/assign.form"><spring:message
				code="privilegehelper.assign" /></a>
	</li>

	<!-- Add further links here -->
</ul>
<h2>
	<spring:message code="privilegehelper.title" />
</h2>

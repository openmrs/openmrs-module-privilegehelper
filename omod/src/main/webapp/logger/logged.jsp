<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>
	<c:choose>
		<c:when test="${loggingPrivileges}">
			<img src="${pageContext.request.contextPath}/images/loading.gif"
				alt="Loading..." />
		Logging privilege checks for ${user.personName}... 
<input type="button" value="Refresh"
				onclick="window.location='logged.form?userId=${user.userId}'" />
			<input type="button" value="Stop logging"
				onclick="window.location='stopLogging.form?userId=${user.userId}'" />
			<input type="button" value="Assign privileges"
				onclick="window.location='../assigner/assign.form?userId=${user.userId}'" />
		</c:when>
		<c:otherwise>
		Privilege checks logged for ${user.personName}: 
			<input type="button" value="Assign privileges"
				onclick="window.location='../assigner/assign.form?userId=${user.userId}'" />
			<input type="button" value="Remove logs"
				onclick="window.location='removeLogged.form?userId=${user.userId}'" />
		</c:otherwise>
	</c:choose>
</p>

<p>
	<c:forEach items="${loggedPrivileges}" var="privilege">
		<c:set var="color" value="green" />
		<c:if test="${privilege.missing}">
			<c:set var="color" value="red" />
		</c:if>
		<openmrs:formatDate date="${privilege.date}" format="MMM d HH:mm:ss:S" />
		<span style="color: ${color}">${privilege.privilege} <c:if
				test="${privilege.missing}">(missing)</c:if></span>in
			${privilege.whereChecked}<br />
	</c:forEach>
</p>

</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
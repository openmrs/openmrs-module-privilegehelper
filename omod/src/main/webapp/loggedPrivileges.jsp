<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<p>
	<c:choose>
		<c:when test="${loggingPrivileges}">Logging privilege checks for ${user.personName}... 
<a href="loggedPrivileges.form?userId=${user.userId}">Refresh</a> <a href="stopLoggingPrivileges.form?userId=${user.userId}">Stop
				logging</a>
		</c:when>
		<c:otherwise>
		Logged privilege checks for ${user.personName}.
			<a href="removeLoggedPrivileges.form?userId=${user.userId}">Remove
				logs</a>
		</c:otherwise>
	</c:choose>
</p>

<p>
<ol>
	<c:forEach items="${loggedPrivileges}" var="privilege">
		<c:set var="color" value="green" />
		<c:if test="${privilege.missing}">
			<c:set var="color" value="red" />
		</c:if>
		<li style="color: ${color}">${privilege.privilege} <c:if
				test="${privilege.missing}">missing</c:if></li>
	</c:forEach>
</ol>
</p>

</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
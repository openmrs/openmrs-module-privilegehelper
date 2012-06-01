<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<c:choose>
	<c:when test="${loggingPrivileges}">
		<p>4. In the other tab/window/browser, refresh the first page of
			the task, then go through the entire task.</p>
		<p>
			5. <input type="button" value="Stop logging"
				onclick="window.location='stopLogging.form?userId=${user.userId}'" />
		</p>
		<p>
			<img src="${pageContext.request.contextPath}/images/loading.gif"
				alt="Loading..." /> Logging privilege checks for
			${user.personName}... <input type="button" value="Refresh"
				onclick="window.location='logged.form?userId=${user.userId}'" />
		</p>
	</c:when>
	<c:otherwise>
		<p>
			<input type="button" value="Assign privileges"
				onclick="window.location='../assigner/assign.form?userId=${user.userId}'" />
			or <input type="button" value="Remove logs"
				onclick="window.location='removeLogged.form?userId=${user.userId}'" />
		</p>
Privilege checks logged for ${user.personName}: 
	</c:otherwise>
</c:choose>

<p>
	Legend: <span style="color: green">has required</span>, <span
		style="color: red">missing required</span>, <span style="color: grey">has
		not required</span>, <span style="color: orange">missing not required</span>
</p>
<p>
	<c:forEach items="${loggedPrivileges}" var="privilege">
		<c:choose>
			<c:when test="${privilege.required and privilege.missing}">
				<c:set var="color" value="red" />
			</c:when>
			<c:when test="${privilege.required and !privilege.missing }">
				<c:set var="color" value="green" />
			</c:when>
			<c:when test="${!privilege.required and privilege.missing}">
				<c:set var="color" value="orange" />
			</c:when>
			<c:when test="${!privilege.required and !privilege.missing}">
				<c:set var="color" value="grey" />
			</c:when>
		</c:choose>

		<openmrs:formatDate date="${privilege.date}" format="MMM d HH:mm:ss:S" />
		<span style="color: ${color};">${privilege.privilege} </span>
		<span style="font-size: xx-small;">in ${privilege.whereChecked}</span>
		<br />
	</c:forEach>
</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
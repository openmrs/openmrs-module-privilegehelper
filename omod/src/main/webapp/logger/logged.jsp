<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/log.form" />

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
				onclick="window.location='../assigner/assignUser.form?loggedUserId=${user.userId}'" />
			or <input type="button" value="Discard this recording"
				onclick="window.location='removeLogged.form?userId=${user.userId}'" />
		</p>
Privilege checks logged for ${user.personName}: 
	</c:otherwise>
</c:choose>

<p>
	Legend: <span style="color: green">* required to perform action -
		already assigned</span>, <span style="color: red">! required to perform
		action - missing</span>, <span style="color: grey">+ enables optional
		feature - already assigned</span>, <span style="color: orange">? enables
		optional feature - missing</span>
</p>
<p>
	<c:forEach items="${loggedPrivileges}" var="privilege">
		<c:choose>
			<c:when test="${privilege.required and privilege.missing}">
				<c:set var="color" value="red" />
				<c:set var="symbol" value="!" />
			</c:when>
			<c:when test="${privilege.required and !privilege.missing }">
				<c:set var="color" value="green" />
				<c:set var="symbol" value="*" />
			</c:when>
			<c:when test="${!privilege.required and privilege.missing}">
				<c:set var="color" value="orange" />
				<c:set var="symbol" value="?" />
			</c:when>
			<c:when test="${!privilege.required and !privilege.missing}">
				<c:set var="color" value="grey" />
				<c:set var="symbol" value="+" />
			</c:when>
		</c:choose>

		<openmrs:formatDate date="${privilege.date}" format="MMM d HH:mm:ss:S" />
		<span style="color: ${color};">${symbol} ${privilege.privilege} </span>
		<span style="font-size: xx-small;">in ${privilege.whereChecked}</span>
		<br />
	</c:forEach>
</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
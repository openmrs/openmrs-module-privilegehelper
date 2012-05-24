<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>
	Inspecting logged privilege checks for ${user.personName}. <a
		href="../logger/logged.form?userId=${user.userId}">Go back to
		logger</a>
</p>

<p>
<form action="addToRole.form" method="POST">
	<input type="hidden" name="userId" value="${user.userId}" />
	<c:forEach items="${privileges}" var="privilege">
		<c:set var="color" value="green" />
		<c:if test="${privilege.missing}">
			<c:set var="color" value="red" />
		</c:if>
		<input type="checkbox" name="privileges"
			value="${privilege.privilege}" checked="checked" />
		<span style="color: ${color}">${privilege.privilege} <c:if
				test="${privilege.missing}">(missing)</c:if></span>
		<br />
	</c:forEach>

	<p>
		Add selected privileges to a role: <input type="text" name="roleId" />
		<input type="submit" />
	</p>
</form>
</p>

</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
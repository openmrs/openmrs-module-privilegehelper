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
<ol>
	<c:forEach items="${privileges}" var="privilege">
		<c:set var="color" value="green" />
		<c:if test="${privilege.missing}">
			<c:set var="color" value="red" />
		</c:if>
		<li style="color: ${color}">${privilege.privilege} <c:if
				test="${privilege.missing}">(missing)</c:if></li>
	</c:forEach>
</ol>
</p>

</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>
	<form:form modelAttribute="privilege" action="addPrivilege.form"
		method="POST">
		Enter privilege you want to assign:
		<form:input path="name" />
		<form:errors path="name" cssClass="error" />
		<input type="submit" value="Add privilege" />
	</form:form>
</p>
<p>
	Entered privileges: <br />
<table>
	<c:forEach items="${privileges}" var="privilege" varStatus="status">
		<form action="removePrivilege.form" method="POST">
			<tr>
				<td>${privilege}</td>
				<td><input type="hidden" name="name" value="${privilege}" /> <input
					type="submit" value="Remove" /></td>
			</tr>
		</form>
	</c:forEach>
</table>
</p>
<c:if test="${not empty missingPrivileges}">
	<p>
		<span clss="error">Some privileges are not present in your:
			system: <c:forEach items="${missingPrivileges}" var="privilege"
				varStatus="status">${privilege}<c:if
					test="${!status.last}">, </c:if>
			</c:forEach>
		</span>
	</p>
</c:if>
<p>
<form action="assignUser.form" method="GET">
	<input type="submit" value="Next" />
</form>
</p>






















</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
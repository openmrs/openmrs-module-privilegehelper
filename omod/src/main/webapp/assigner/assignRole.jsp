<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/assigner/assignUser.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<form:form modelAttribute="role">
	<p>Create a new role and assign it to ${user.personName}.</p>
	<table>
		<tr>
			<td>Name:</td>
			<td><form:input path="role" /> <form:errors path="role"
					cssClass="error" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><form:input path="description" /> <form:errors
					path="description" cssClass="error" /></td>
		</tr>
	</table>
	<p>
		<input type="button" value="Cancel"
			onclick="window.location='assignRoles.form?userId=${user.userId}'" />
		<input type="submit" value="Create and assign role" />
	</p>
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
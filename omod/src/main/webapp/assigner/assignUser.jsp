<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/assigner/assignUser.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<form:form modelAttribute="user">
	<p>
		Pick a user who has the roles you want to assign privileges to:
		<openmrs:fieldGen type="org.openmrs.User" formFieldName="userId"
			val="" />
		<form:errors path="userId" cssClass="error" />
	</p>
	<p>You will have a chance to confirm before making any changes.</p>
	<input type="submit" value="Next" />
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
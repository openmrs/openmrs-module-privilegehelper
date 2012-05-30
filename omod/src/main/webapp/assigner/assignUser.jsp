<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<form:form modelAttribute="user">
	<p>
		Enter user you want to assign privileges to:
		<openmrs:fieldGen type="org.openmrs.User" formFieldName="userId"
			val="" />
		<form:errors path="userId" cssClass="error" />
	</p>
	<input type="submit" value="Next" />
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
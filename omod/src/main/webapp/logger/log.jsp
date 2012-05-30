<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>This page allows you to log privileges which are checked when the
	system is used by users.</p>

<p>
	<form:form modelAttribute="user">
	Enter user:
	<openmrs:fieldGen type="org.openmrs.User" formFieldName="userId" val="" />
		<form:errors path="userId" cssClass="error" />
		<input type="submit" value="Start logging" />
	</form:form>
</p>

<ol>
	<c:forEach items="${loggedUsers}" var="user">
		<li>${user.personName} <input type="button"
			onclick="window.location='logged.form?userId=${user.userId}'"
			value="View logs" />
		</li>
	</c:forEach>
</ol>
</p>

<%@ include file="/WEB-INF/template/footer.jsp"%>
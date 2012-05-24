<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>This module allows you to log privileges which are checked when
	the system is used by your users.</p>

<p>
<form action="log.form" method="POST">
	Choose a user to observe:
	<openmrs:fieldGen type="org.openmrs.User" formFieldName="userId" val="" />
	<input type="submit" />
</form>
</p>

<p>Observed users:
<ol>
	<c:forEach items="${loggedUsers}" var="user">
		<li><a href="logged.form?userId=${user.userId}">${user.personName}</a>
		</li>
	</c:forEach>
</ol>
</p>

<%@ include file="/WEB-INF/template/footer.jsp"%>
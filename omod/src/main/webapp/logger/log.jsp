<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/log.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>This tool helps you figure out what privileges are required to
	perform a particular task, and then helps you assign those privileges
	to roles.</p>

<form:form modelAttribute="user">
	<p>
		1. Choose a user that you will use to perform the task:
		<openmrs:fieldGen type="org.openmrs.User" formFieldName="userId"
			val="${currentUser}" />
		<form:errors path="userId" cssClass="error" />
	</p>	
	<p>2. In another tab/window/browser, have that user open the first
		page of their task.</p>
	<p>
		3. <input type="submit" value="Start logging" />
	</p>
</form:form>

<c:if test="${!empty loggedUsers}">
	<h3>Previously logged users:</h3>
	<ol>
		<c:forEach items="${loggedUsers}" var="user">
			<li>${user.personName} <input type="button"
				onclick="window.location='logged.form?userId=${user.userId}'"
				value="View logs" />
			</li>
		</c:forEach>
	</ol>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp"%>
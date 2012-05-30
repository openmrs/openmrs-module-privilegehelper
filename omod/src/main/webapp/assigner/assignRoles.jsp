<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/logger/manage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<p>Assigning privileges to user ${user.personName}.</p>
<p>
	<form:form modelAttribute="role">
Enter role to assign:
<input type="hidden" name="assignRole" value="true"/>
<form:input path="name" />
		<input type="submit" value="Assign role" />
	</form:form>
</p>


<form action="assignRoles.form" method="POST">
	<input type="hidden" name="userId" value="${user.userId}" />
	<table>
		<thead>
			<tr>
				<th></th>
				<c:forEach items="${roles}" var="role" varStatus="status">
					<th>${role}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${rolesByPrivileges}" var="rolesByPrivilege">
				<tr>
					<td><c:set var="color" value="red" /> <c:if
							test="${rolesByPrivilege.value[0]}">
							<c:set var="color" value="green" />
						</c:if> <span style="color: ${color}">${rolesByPrivilege.key}</span></td>
					<c:forEach items="${roles}" var="role" varStatus="status">
						<c:set var="checked" value="" />
						<c:if test="${rolesByPrivilege.value[status.index+1]}">
							<c:set var="checked" value="checked" />
						</c:if>
						<td><input type="checkbox" name="${rolesByPrivilege.key}"
							value="${role}" ${checked} /></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<input type="submit" value="Assign" />
</form>
</p>

























</p>
<%@ include file="/WEB-INF/template/footer.jsp"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/assigner/assignUser.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/privilegehelper/scripts/selectItems.js" />

<p>
	<form:form modelAttribute="role">
		<fieldset>
			<legend>Assigning new role to ${user.personName}</legend>
			<p></p>
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
				<input type="submit" name="assignRole" value="Assign role" />
			</p>
		</fieldset>
	</form:form>
</p>
<p>
<form action="assignRoles.form" method="POST">
	<fieldset>
		<legend>Assigning privileges to roles for ${user.personName}</legend>
		<p>
			Legend: <span style="color: green">has required</span>, <span
				style="color: red">missing required</span>, <span
				style="color: grey">has not required</span>, <span
				style="color: orange">missing not required</span>
		</p>
		<input type="hidden" name="userId" value="${user.userId}" />
		<table>
			<thead>
				<tr>
					<th></th>
					<th colspan="${fn:length(roles)-1}" style="text-align: center">Current
						Roles</th>
				</tr>
				<tr>
					<th></th>
					<c:forEach items="${roles}" var="role" varStatus="status">
						<th>${role} <select name="selectItems" id="${role}">
								<option>select</option>
								<option>none</option>
								<option>missing required</option>
								<option>all required</option>
								<option>all missing</option>
								<option>all</option>
						</select>
						</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${rolesByPrivileges}" var="rolesByPrivilege">
					<tr>
						<td><c:choose>
								<c:when
									test="${rolesByPrivilege.key.required and rolesByPrivilege.key.missing}">
									<c:set var="color" value="red" />
								</c:when>
								<c:when
									test="${rolesByPrivilege.key.required and !rolesByPrivilege.key.missing }">
									<c:set var="color" value="green" />
								</c:when>
								<c:when
									test="${!rolesByPrivilege.key.required and rolesByPrivilege.key.missing}">
									<c:set var="color" value="orange" />
								</c:when>
								<c:when
									test="${!rolesByPrivilege.key.required and !rolesByPrivilege.key.missing}">
									<c:set var="color" value="grey" />
								</c:when>
							</c:choose><span style="color: ${color}">${rolesByPrivilege.key.privilege}</span></td>
						<c:forEach items="${roles}" var="role" varStatus="status">
							<td><c:choose>
									<c:when test="${rolesByPrivilege.value[status.index]}">
										<c:choose>
											<c:when
												test="${rolesByPrivilege.key.required and rolesByPrivilege.key.missing}">
												<c:set var="class" value="missing required" />
											</c:when>
											<c:when
												test="${rolesByPrivilege.key.required and !rolesByPrivilege.key.missing}">
												<c:set var="class" value="required" />
											</c:when>
											<c:when
												test="${!rolesByPrivilege.key.required and rolesByPrivilege.key.missing }">
												<c:set var="class" value="missing" />
											</c:when>
											<c:otherwise>
												<c:set var="class" value="" />
											</c:otherwise>
										</c:choose>

										<input type="checkbox"
											name="${rolesByPrivilege.key.privilege}" value="${role}"
											class="${class}" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" disabled checked />
									</c:otherwise>
								</c:choose></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${!empty missingPrivileges}">
			<p>
				Some privileges do not exist:
				<c:forEach items="${missingPrivileges}" var="privilege"
					varStatus="status">
					<c:choose>
						<c:when
							test="${rolesByPrivilege.key.required and rolesByPrivilege.key.missing}">
							<c:set var="color" value="red" />
						</c:when>
						<c:when
							test="${rolesByPrivilege.key.required and !rolesByPrivilege.key.missing }">
							<c:set var="color" value="green" />
						</c:when>
						<c:when
							test="${!rolesByPrivilege.key.required and rolesByPrivilege.key.missing}">
							<c:set var="color" value="orange" />
						</c:when>
						<c:when
							test="${!rolesByPrivilege.key.required and !rolesByPrivilege.key.missing}">
							<c:set var="color" value="grey" />
						</c:when>
					</c:choose>
					<span style="color: ${color}">${privilege.privilege}</span>
					<c:if test="${!status.last}">, </c:if>
				</c:forEach>
			</p>
		</c:if>
		<p>
			<input type="submit" value="Save changes" />
		</p>
	</fieldset>
</form>
</p>

<%@ include file="/WEB-INF/template/footer.jsp"%>
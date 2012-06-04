<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm"
	redirect="/module/privilegehelper/assigner/assignUser.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>

<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/privilegehelper/scripts/selectItems.js" />

<form action="assignRoles.form" method="POST">
	<p>Assigning privileges to roles for ${user.personName}</p>
	<p>
		Legend: <span style="color: green">required to perform action -
			already assigned</span>, <span style="color: red">required to perform
			action - missing</span>, <span style="color: grey">enables optional
			feature - already assigned</span>, <span style="color: orange">enables
			optional feature - missing</span>
	</p>
	<input type="hidden" name="userId" value="${user.userId}" />
	<table>
		<thead>
			<tr>
				<th></th>
				<th colspan="${fn:length(roles)}" style="text-align: center">Current
					Roles</th>
				<th></th>
			</tr>
			<tr>
				<th></th>
				<c:forEach items="${roles}" var="role" varStatus="status">
					<th style="text-align: center">${role} <br /> <select
						name="selectItems" id="${role}">
							<option>select</option>
							<option>none</option>
							<option>missing & required</option>
							<option>all required</option>
							<option>all</option>
					</select>
					</th>
				</c:forEach>
				<th><input type="button"
					value="Create a new role and assign it to ${user.personName}"
					onclick="window.location='assignRole.form?userId=${user.userId}'" /></th>
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

									<input type="checkbox" name="${rolesByPrivilege.key.privilege}"
										value="${role}" class="${class}" />
								</c:when>
								<c:otherwise>
									<span style="font-size: xx-small;">already assigned</span>
								</c:otherwise>
							</c:choose></td>
					</c:forEach>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${!empty missingPrivileges}">
		<p>
			Some privileges do not exist:
			<c:forEach items="${missingPrivileges}" var="privilege"
				varStatus="status">
				${privilege.privilege}<c:if test="${!status.last}">, </c:if>
			</c:forEach>
		</p>
	</c:if>
	<p>
		<input type="submit" value="Save changes" />
	</p>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
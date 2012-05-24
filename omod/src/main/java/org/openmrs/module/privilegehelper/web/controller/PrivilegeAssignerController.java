/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.privilegehelper.web.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.privilegehelper.PrivilegeHelperWebConstants;
import org.openmrs.module.privilegehelper.PrivilegeLogEntry;
import org.openmrs.module.privilegehelper.PrivilegeLogger;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The privilege assigning controller.
 */
@Controller
@RequestMapping(value = PrivilegeHelperWebConstants.MODULE_URL + "/assigner")
public class PrivilegeAssignerController {
	
	@Autowired
	PrivilegeLogger logger;
	
	@RequestMapping(value = "/inspect", method = RequestMethod.GET)
	public void inspect(final Integer userId, final ModelMap model) {
		User user = getUser(userId);
		final List<PrivilegeLogEntry> loggedPrivileges = logger.getLoggedPrivileges(user);
		
		final Set<PrivilegeLogEntry> privileges = new LinkedHashSet<PrivilegeLogEntry>();
		privileges.addAll(loggedPrivileges);
		
		model.addAttribute("user", user);
		model.addAttribute("privileges", privileges);
	}
	
	@RequestMapping(value = "/addToRole", method = RequestMethod.POST)
	public String addToRole(final Integer userId, final String roleId, final String[] privileges, final ModelMap model,
	                        final HttpSession session) {
		model.addAttribute("userId", userId);
		
		Role role = Context.getUserService().getRole(roleId);
		if (role == null) {
			session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Role " + roleId + " does not exist!");
			return "redirect:inspect.form";
		}
		
		for (String privilege : privileges) {
			Privilege privilegeObject = Context.getUserService().getPrivilege(privilege);
			role.addPrivilege(privilegeObject);
		}
		
		Context.getUserService().saveRole(role);
		
		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Privileges " + StringUtils.join(privileges, ", ")
		        + " added to role " + roleId + ".");
		return "redirect:inspect.form";
	}
	
	public User getUser(Integer userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId must not be null");
		}
		return Context.getUserService().getUser(userId);
	}
}

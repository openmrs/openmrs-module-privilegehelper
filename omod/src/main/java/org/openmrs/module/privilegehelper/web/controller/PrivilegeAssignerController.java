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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.privilegehelper.PrivilegeHelperWebConstants;
import org.openmrs.module.privilegehelper.PrivilegeLogEntry;
import org.openmrs.module.privilegehelper.PrivilegeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The privilege assigning controller.
 */
@Controller
@SessionAttributes(PrivilegeAssignerController.PRIVILEGES)
@RequestMapping(value = PrivilegeHelperWebConstants.MODULE_URL + "/assigner")
public class PrivilegeAssignerController {
	
	public static final String PRIVILEGES = "privileges";
	
	@Autowired
	PrivilegeLogger logger;
	
	@ModelAttribute(PRIVILEGES)
	public Set<String> getPrivileges() {
		return new LinkedHashSet<String>();
	}
	
	@ModelAttribute
	public Role getRole(@RequestParam(required = false) String name) {
		if (name == null) {
			return new Role();
		} else {
			Role role = Context.getUserService().getRole(name);
			return (role != null) ? role : new Role();
		}
	}
	
	@ModelAttribute
	public Privilege getPrivilege(@RequestParam(required = false) String name) {
		if (name == null) {
			return new Privilege();
		} else {
			Privilege privilege = Context.getUserService().getPrivilege(name);
			if (privilege == null) {
				return new Privilege();
			} else {
				return privilege;
			}
		}
	}
	
	@ModelAttribute
	public User getUser(@RequestParam(required = false) Integer userId) {
		if (userId == null) {
			return new User();
		} else {
			User user = Context.getUserService().getUser(userId);
			return (user != null) ? user : new User();
		}
	}
	
	@RequestMapping(value = "/assign")
	public void assign(@ModelAttribute(PRIVILEGES) Set<String> privileges, User user, Model model) {
		if (user.getUserId() != null) {
			Set<String> missingPrivileges = new LinkedHashSet<String>();
			
			List<PrivilegeLogEntry> loggedPrivileges = logger.getLoggedPrivileges(user);
			for (PrivilegeLogEntry privilege : loggedPrivileges) {
				if (StringUtils.isBlank(privilege.getPrivilege())) {
					continue;
				}
				
				Privilege existingPrivilege = Context.getUserService().getPrivilege(privilege.getPrivilege());
				
				if (existingPrivilege != null) {
					privileges.add(existingPrivilege.getName());
				} else {
					missingPrivileges.add(privilege.getPrivilege());
				}
			}
			
			model.addAttribute("missingPrivileges", missingPrivileges);
		}
	}
	
	@RequestMapping(value = "/addPrivilege", method = RequestMethod.POST)
	public String addPrivilege(Privilege privilege, Errors errors, @ModelAttribute(PRIVILEGES) Set<String> privileges,
	                           ModelMap model) {
		if (privilege.getName() == null) {
			errors.rejectValue("name", "privilegehelper.privilege.invalid", "You must enter a valid privilege");
			return PrivilegeHelperWebConstants.MODULE_URL + "/assigner/assign";
		}
		
		privileges.add(privilege.getName());
		
		model.clear();
		return "redirect:assign.form";
	}
	
	@RequestMapping(value = "/removePrivilege", method = RequestMethod.POST)
	public String removePrivilege(Privilege privilege, @ModelAttribute(PRIVILEGES) Set<String> privileges, ModelMap model) {
		if (privilege.getName() != null) {
			privileges.remove(privilege.getName());
		}
		
		model.clear();
		return "redirect:assign.form";
	}
	
	@RequestMapping(value = "/assignUser", method = RequestMethod.GET)
	public void assignUser() {
	}
	
	@RequestMapping(value = "/assignUser", method = RequestMethod.POST)
	public String assignUser(User user, Errors errors, ModelMap model) {
		if (user.getUserId() == null) {
			errors.rejectValue("userId", "privilegehelper.user.invalid", "You must enter a valid user");
			return null;
		}
		
		model.addAttribute("userId", user.getUserId());
		return "redirect:assignRoles.form";
	}
	
	@RequestMapping(value = "/assignRoles", method = RequestMethod.GET)
	public void assignRoles(@ModelAttribute(PRIVILEGES) Set<String> privileges, User user, ModelMap model) {
		Map<String, Boolean[]> rolesByPrivileges = new LinkedHashMap<String, Boolean[]>();
		
		SortedSet<Role> userRoles = new TreeSet<Role>(new Comparator<Role>() {
			
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		userRoles.addAll(user.getAllRoles());
		
		for (String privilege : privileges) {
			Boolean[] roles = new Boolean[userRoles.size() + 1];
			roles[0] = user.hasPrivilege(privilege);
			
			int i = 1;
			for (Role role : userRoles) {
				roles[i++] = role.hasPrivilege(privilege);
			}
			
			rolesByPrivileges.put(privilege, roles);
		}
		
		Set<String> roles = new LinkedHashSet<String>();
		for (Role role : userRoles) {
			roles.add(role.getName());
		}
		
		model.addAttribute("roles", roles);
		model.addAttribute("rolesByPrivileges", rolesByPrivileges);
		model.addAttribute("user", user);
	}
	
	@RequestMapping(value = "/assignRoles", method = RequestMethod.POST)
	public void assignRolesPOST(@ModelAttribute(PRIVILEGES) Set<String> privileges, User user, HttpServletRequest request,
	                            Role role, @RequestParam(required = false) Boolean assignRole, ModelMap model) {
		if (assignRole != null) {
			if (role.getName() != null) {
				user.addRole(role);
			}
		} else {
			for (String privilege : privileges) {
				String[] roles = request.getParameterValues(privilege);
				
				for (String existingRole : roles) {
					Privilege privilegeObject = Context.getUserService().getPrivilege(privilege);
					
					Role roleObject = Context.getUserService().getRole(existingRole);
					roleObject.addPrivilege(privilegeObject);
					
					Context.getUserService().saveRole(roleObject);
				}
			}
		}
		
		model.clear();
		assignRoles(privileges, user, model);
	}
}

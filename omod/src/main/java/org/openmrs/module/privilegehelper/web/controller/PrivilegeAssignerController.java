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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
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
import org.openmrs.validator.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@SessionAttributes(value = { PrivilegeAssignerController.PRIVILEGES, PrivilegeAssignerController.MISSING_PRIVILEGES })
@RequestMapping(value = PrivilegeHelperWebConstants.MODULE_URL + "/assigner")
public class PrivilegeAssignerController {
	
	public static final String PRIVILEGES = "privileges";
	
	public static final String MISSING_PRIVILEGES = "missingPrivileges";
	
	@Autowired
	PrivilegeLogger logger;
	
	@ModelAttribute(PRIVILEGES)
	public SortedSet<PrivilegeLogEntry> getPrivileges() {
		return new TreeSet<PrivilegeLogEntry>();
	}
	
	@ModelAttribute(MISSING_PRIVILEGES)
	public SortedSet<PrivilegeLogEntry> getMissingPrivileges() {
		return new TreeSet<PrivilegeLogEntry>();
	}
	
	@ModelAttribute
	public Role getRole() {
		return new Role();
	}
	
	@ModelAttribute
	public Privilege getPrivilege(final @RequestParam(required = false) String name) {
		if (name == null) {
			return new Privilege();
		} else {
			final Privilege privilege = Context.getUserService().getPrivilege(name);
			return (privilege != null) ? privilege : new Privilege();
		}
	}
	
	@ModelAttribute
	public User getUser(final @RequestParam(required = false) Integer userId) {
		if (userId == null) {
			return new User();
		} else {
			User user = Context.getUserService().getUser(userId);
			return (user != null) ? user : new User();
		}
	}
	
	@RequestMapping(value = "/assignPrivileges")
	public String assignPrivileges(final @ModelAttribute(PRIVILEGES) SortedSet<PrivilegeLogEntry> privileges,
	                               final @ModelAttribute(MISSING_PRIVILEGES) SortedSet<PrivilegeLogEntry> missingPrivileges,
	                               final User user, @RequestParam final Integer loggedUserId, final ModelMap model) {
		User loggedUser = Context.getUserService().getUser(loggedUserId);
		if (loggedUser == null) {
			throw new IllegalArgumentException("User with id " + loggedUserId + " does not exist!");
		}
		
		final List<PrivilegeLogEntry> loggedPrivileges = logger.getLoggedPrivileges(loggedUser);
		
		for (PrivilegeLogEntry privilege : loggedPrivileges) {
			if (StringUtils.isBlank(privilege.getPrivilege())) {
				continue;
			}
			
			final Privilege existingPrivilege = Context.getUserService().getPrivilege(privilege.getPrivilege());
			
			if (existingPrivilege != null) {
				privileges.add(new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), privilege.isRequired(),
				        !user.hasPrivilege(privilege.getPrivilege())));
			} else {
				missingPrivileges.add(new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), privilege
				        .isRequired(), !user.hasPrivilege(privilege.getPrivilege())));
			}
		}
		
		model.addAttribute("userId", user.getUserId());
		return "redirect:assignRoles.form";
	}
	
	@RequestMapping(value = "/assignUser", method = RequestMethod.GET)
	public void assignUser(@RequestParam(required = false) final Integer loggedUserId, final ModelMap model) {
		model.addAttribute("loggedUserId", loggedUserId);
	}
	
	@RequestMapping(value = "/assignUser", method = RequestMethod.POST)
	public String assignUser(@RequestParam(required = false) final Integer loggedUserId, final User user,
	                         final Errors errors, final ModelMap model) {
		if (user.getUserId() == null) {
			errors.rejectValue("userId", "privilegehelper.user.invalid", "You must enter a valid user");
			return null;
		}
		
		model.addAttribute("userId", user.getUserId());
		if (loggedUserId != null) {
			model.addAttribute("loggedUserId", loggedUserId);
		}
		return "redirect:assignPrivileges.form";
	}
	
	@RequestMapping(value = "/assignRoles", method = RequestMethod.GET)
	public void assignRoles(@ModelAttribute(PRIVILEGES) final SortedSet<PrivilegeLogEntry> privileges,
	                        @ModelAttribute(MISSING_PRIVILEGES) final SortedSet<PrivilegeLogEntry> missingPrivileges,
	                        final User user, final ModelMap model) {
		Map<PrivilegeLogEntry, Boolean[]> rolesByPrivileges = new TreeMap<PrivilegeLogEntry, Boolean[]>();
		
		SortedSet<Role> userRoles = new TreeSet<Role>(new Comparator<Role>() {
			
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		userRoles.addAll(user.getAllRoles());
		
		for (PrivilegeLogEntry privilege : privileges) {
			Boolean[] roles = new Boolean[userRoles.size()];
			
			int i = 0;
			for (Role role : userRoles) {
				roles[i++] = !role.hasPrivilege(privilege.getPrivilege());
			}
			
			if (privilege.isRequired()) {
				//Remove not required privilege if there's a required one.
				rolesByPrivileges.remove(new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), false, !user
				        .hasPrivilege(privilege.getPrivilege())));
				
				rolesByPrivileges.put(
				    new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), privilege.isRequired(), !user
				            .hasPrivilege(privilege.getPrivilege())), roles);
			} else {
				//Add not required privilege only if there's no required one.
				if (!rolesByPrivileges.containsKey(new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), true,
				        !user.hasPrivilege(privilege.getPrivilege())))) {
					rolesByPrivileges.put(
					    new PrivilegeLogEntry(user.getUserId(), privilege.getPrivilege(), privilege.isRequired(), !user
					            .hasPrivilege(privilege.getPrivilege())), roles);
				}
			}
			
		}
		
		SortedSet<String> roles = new TreeSet<String>();
		for (Role role : userRoles) {
			roles.add(role.getName());
		}
		
		model.addAttribute("roles", roles);
		model.addAttribute("rolesByPrivileges", rolesByPrivileges);
		model.addAttribute("user", user);
	}
	
	@RequestMapping(value = "/assignRoles", method = RequestMethod.POST)
	public void assignRolesPOST(@ModelAttribute(PRIVILEGES) final SortedSet<PrivilegeLogEntry> privileges,
	                            @ModelAttribute(MISSING_PRIVILEGES) final SortedSet<PrivilegeLogEntry> missingPrivileges,
	                            final User user, final HttpServletRequest request, final Role role, final Errors errors,
	                            @RequestParam(required = false) final String assignRole, final ModelMap model) {
		if (assignRole != null) {
			final RoleValidator validator = new RoleValidator();
			validator.validate(role, errors);
			if (errors.hasErrors()) {
				assignRoles(privileges, missingPrivileges, user, model);
				return;
			}
			
			Role existingRole = Context.getUserService().getRole(role.getRole());
			if (existingRole != null) {
				errors.rejectValue("role", "role.exists.error", "Role with the given name already exists!");
				assignRoles(privileges, missingPrivileges, user, model);
				return;
			}
			
			Context.getUserService().saveRole(role);
			user.addRole(role);
		} else {
			for (PrivilegeLogEntry privilege : privileges) {
				String[] roles = request.getParameterValues(privilege.getPrivilege());
				
				if (roles == null) {
					continue;
				}
				
				for (String existingRole : roles) {
					Privilege privilegeObject = Context.getUserService().getPrivilege(privilege.getPrivilege());
					
					Role roleObject = Context.getUserService().getRole(existingRole);
					roleObject.addPrivilege(privilegeObject);
					
					Context.getUserService().saveRole(roleObject);
				}
			}
		}
		
		assignRoles(privileges, missingPrivileges, user, model);
	}
}

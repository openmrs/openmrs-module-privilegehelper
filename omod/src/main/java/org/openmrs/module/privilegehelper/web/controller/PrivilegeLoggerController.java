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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.privilegehelper.PrivilegeHelperWebConstants;
import org.openmrs.module.privilegehelper.PrivilegeLogEntry;
import org.openmrs.module.privilegehelper.PrivilegeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The privilege logging controller.
 */
@Controller
@RequestMapping(value = PrivilegeHelperWebConstants.MODULE_URL + "/logger")
public class PrivilegeLoggerController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	PrivilegeLogger logger;
	
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public void log(ModelMap model) {
		model.addAttribute("loggedUsers", logger.getLoggedUsers());
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
	
	@RequestMapping(value = "/log", method = RequestMethod.POST)
	public String logPrivileges(User user, Errors errors, ModelMap model) {
		if (user.getUserId() == null) {
			errors.rejectValue("userId", "privilegehelper.user.invalid", "You must enter a valid user");
			return null;
		}
		
		logger.logPrivileges(user);
		
		model.addAttribute("userId", user.getUserId());
		return "redirect:logged.form";
	}
	
	@RequestMapping(value = "/stopLogging", method = RequestMethod.GET)
	public String stopLoggingPrivileges(User user, ModelMap model) {
		List<PrivilegeLogEntry> loggedPrivileges = logger.stopLoggingPrivileges(user);
		
		populateModelForLoggedPrivileges(user, loggedPrivileges, model);
		return PrivilegeHelperWebConstants.MODULE_URL + "/logger/logged";
	}
	
	@RequestMapping(value = "/logged", method = RequestMethod.GET)
	public void loggedPrivileges(User user, ModelMap model) {
		List<PrivilegeLogEntry> loggedPrivileges = logger.getLoggedPrivileges(user);
		
		populateModelForLoggedPrivileges(user, loggedPrivileges, model);
	}
	
	@RequestMapping(value = "/removeLogged", method = RequestMethod.GET)
	public String removeLoggedPrivileges(User user) {
		logger.removeLoggedPrivileges(user);
		
		return "redirect:log.form";
	}
	
	public void populateModelForLoggedPrivileges(User user, List<PrivilegeLogEntry> loggedPrivileges, ModelMap model) {
		model.addAttribute("user", user);
		model.addAttribute("loggedPrivileges", loggedPrivileges);
		model.addAttribute("loggingPrivileges", logger.isLoggingPrivileges(user));
	}
	
}

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The privilege logging controller.
 */
@Controller
@RequestMapping(value = PrivilegeHelperWebConstants.MODULE_URL + "/logger")
public class PrivilegeLoggerController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	PrivilegeLogger logger;
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("loggedUsers", logger.getLoggedUsers());
	}
	
	@RequestMapping(value = "/logPrivileges", method = RequestMethod.POST)
	public String logPrivileges(Integer userId, ModelMap model) {
		logger.logPrivileges(getUser(userId));
		
		model.addAttribute("userId", userId);
		return "redirect:loggedPrivileges.form";
	}
	
	@RequestMapping(value = "/stopLoggingPrivileges", method = RequestMethod.GET)
	public String stopLoggingPrivileges(Integer userId, ModelMap model) {
		User user = getUser(userId);
		List<PrivilegeLogEntry> loggedPrivileges = logger.stopLoggingPrivileges(user);
		
		populateModelForLoggedPrivileges(user, loggedPrivileges, model);
		return PrivilegeHelperWebConstants.MODULE_URL + "/logger/loggedPrivileges";
	}
	
	@RequestMapping(value = "/loggedPrivileges", method = RequestMethod.GET)
	public void loggedPrivileges(Integer userId, ModelMap model) {
		User user = getUser(userId);
		List<PrivilegeLogEntry> loggedPrivileges = logger.getLoggedPrivileges(user);
		
		populateModelForLoggedPrivileges(user, loggedPrivileges, model);
	}
	
	@RequestMapping(value = "/removeLoggedPrivileges", method = RequestMethod.GET)
	public String removeLoggedPrivileges(Integer userId) {
		logger.removeLoggedPrivileges(getUser(userId));
		
		return "redirect:manage.form";
	}
	
	public void populateModelForLoggedPrivileges(User user, List<PrivilegeLogEntry> loggedPrivileges, ModelMap model) {
		model.addAttribute("user", user);
		model.addAttribute("loggedPrivileges", loggedPrivileges);
		model.addAttribute("loggingPrivileges", logger.isLoggingPrivileges(user));
	}
	
	public User getUser(Integer userId) {
		return Context.getUserService().getUser(userId);
	}
}

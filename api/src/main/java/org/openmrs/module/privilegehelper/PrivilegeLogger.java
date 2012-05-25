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
package org.openmrs.module.privilegehelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmrs.PrivilegeListener;
import org.openmrs.User;
import org.openmrs.aop.AuthorizationAdvice;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.web.taglib.PrivilegeTag;
import org.openmrs.web.taglib.RequireTag;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Logs any privilege checks.
 */
@Component(PrivilegeHelperActivator.MODULE_ID + ".PrivilegeLogger")
public class PrivilegeLogger implements PrivilegeListener {
	
	private final Map<Integer, PrivilegeLog> logByUserId;
	
	private final Set<String> ignoredClasses;
	
	public PrivilegeLogger() {
		logByUserId = new ConcurrentHashMap<Integer, PrivilegeLog>();
		ignoredClasses = new HashSet<String>();
		
		ignoredClasses.add(AuthorizationAdvice.class.getName());
		ignoredClasses.add(Context.class.getName());
		ignoredClasses.add(RequireTag.class.getName());
		ignoredClasses.add(PrivilegeTag.class.getName());
	}
	
	private class PrivilegeLog {
		
		private final List<PrivilegeLogEntry> privileges = new CopyOnWriteArrayList<PrivilegeLogEntry>();
		
		private volatile boolean active = true;
		
		/**
		 * @return the active
		 */
		public boolean isActive() {
			return active;
		}
		
		/**
		 * @param active the active to set
		 */
		public void setActive(final boolean active) {
			this.active = active;
		}
		
		/**
		 * @return the privileges
		 */
		public List<PrivilegeLogEntry> getPrivileges() {
			return privileges;
		}
		
	}
	
	/**
	 * @see org.openmrs.PrivilegeListener#privilegeChecked(org.openmrs.User, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void privilegeChecked(final User user, final String privilege, final boolean hasPrivilege) {
		if (user == null)
			return;
		
		final PrivilegeLog log = logByUserId.get(user.getUserId());
		
		if (log == null || !log.isActive())
			return;
		
		final String where = findWhereChecked();
		
		log.getPrivileges().add(new PrivilegeLogEntry(user.getUserId(), privilege, !hasPrivilege, where));
	}
	
	/**
	 * Inspects the stack trace to find a place where the privilege was checked
	 * 
	 * @return the class.method or <code>null</code> if it cannot be found
	 */
	private String findWhereChecked() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			final StackTraceElement unrelatedElement = stackTrace[i];
			
			if (UserContext.class.getName().equals(unrelatedElement.getClassName())
			        && "hasPrivilege".equals(unrelatedElement.getMethodName())) {
				
				for (int j = i + 1; j < stackTrace.length; j++) {
					final StackTraceElement element = stackTrace[j];
					
					if (element.getFileName() != null && element.getFileName().endsWith("_jsp")) {
						String jsp = element.getFileName();
						int indexOfView = jsp.indexOf("view");
						if (indexOfView > 0) {
							jsp = jsp.substring(indexOfView);
						}
						jsp = jsp.replace(".", "/");
						jsp = jsp.replace("_", ".");
						return jsp;
					}
					
					if (!element.getClassName().startsWith("org.openmrs")) {
						continue;
					}
					
					if (ignoredClasses.contains(element.getClassName())) {
						continue;
					}
					
					try {
						final Class<?> clazz = OpenmrsClassLoader.getInstance().loadClass(element.getClassName());
						
						if (clazz.isAnnotationPresent(Controller.class)) {
							String url = "";
							
							final RequestMapping clazzRequestMapping = clazz.getAnnotation(RequestMapping.class);
							if (clazzRequestMapping != null) {
								url = clazzRequestMapping.value()[0];
							}
							
							final Method[] methods = clazz.getMethods();
							for (Method method : methods) {
								if (method.getName().equals(element.getMethodName())) {
									final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
									if (requestMapping != null) {
										url += requestMapping.value()[0];
									}
									break;
								}
							}
							
							if (url.isEmpty()) {
								return element.toString();
							} else {
								return element.toString() + ", URL: " + url;
							}
						}
					}
					catch (ClassNotFoundException e) {}
					
					return element.toString();
				}
			}
		}
		return null;
	}
	
	public void logPrivileges(final User user) {
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		
		logByUserId.put(user.getUserId(), new PrivilegeLog());
	}
	
	public List<User> getLoggedUsers() {
		final Set<Integer> userIds = logByUserId.keySet();
		
		final List<User> users = new ArrayList<User>();
		
		for (Integer userId : userIds) {
			User user = Context.getUserService().getUser(userId);
			users.add(user);
		}
		
		Collections.sort(users, new Comparator<User>() {
			
			@Override
			public int compare(User o1, User o2) {
				return o1.getUserId().compareTo(o2.getUserId());
			}
			
		});
		
		return users;
	}
	
	public List<PrivilegeLogEntry> getLoggedPrivileges(final User user) {
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		
		final PrivilegeLog log = logByUserId.get(user.getUserId());
		
		if (log == null)
			return Collections.emptyList();
		
		return log.getPrivileges();
	}
	
	public boolean isLoggingPrivileges(final User user) {
		if (user == null)
			throw new IllegalArgumentException("User mut not be null");
		
		final PrivilegeLog log = logByUserId.get(user.getUserId());
		
		if (log == null)
			return false;
		
		return log.isActive();
	}
	
	public List<PrivilegeLogEntry> stopLoggingPrivileges(final User user) {
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		
		final PrivilegeLog log = logByUserId.get(user.getUserId());
		
		if (log == null)
			return Collections.emptyList();
		
		log.setActive(false);
		return log.getPrivileges();
	}
	
	public void removeLoggedPrivileges(final User user) {
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		
		logByUserId.remove(user.getUserId());
	}
}

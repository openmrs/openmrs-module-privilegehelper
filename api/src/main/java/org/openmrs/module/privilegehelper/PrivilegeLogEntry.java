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

import java.io.Serializable;

/**
 * Represents a single log entry.
 */
public class PrivilegeLogEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Integer userId;
	
	private final String privilege;
	
	private final boolean missing;
	
	private final String whereChecked;
	
	public PrivilegeLogEntry(Integer userId, String privilege, boolean missing, String whereChecked) {
		this.userId = userId;
		this.privilege = privilege;
		this.missing = missing;
		this.whereChecked = whereChecked;
	}
	
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * @return the privilege
	 */
	public String getPrivilege() {
		return privilege;
	}
	
	/**
	 * @return the missing
	 */
	public boolean isMissing() {
		return missing;
	}
	
	/**
	 * Provided for JSTL use only.
	 * 
	 * @return the missing
	 */
	public Boolean getIsMissing() {
		return missing;
	}
	
	/**
	 * @return the whereChecked
	 */
	public String getWhereChecked() {
		return whereChecked;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + ((privilege == null) ? 0 : privilege.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((whereChecked == null) ? 0 : whereChecked.hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivilegeLogEntry other = (PrivilegeLogEntry) obj;
		if (missing != other.missing)
			return false;
		if (privilege == null) {
			if (other.privilege != null)
				return false;
		} else if (!privilege.equals(other.privilege))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (whereChecked == null) {
			if (other.whereChecked != null)
				return false;
		} else if (!whereChecked.equals(other.whereChecked))
			return false;
		return true;
	}
	
}

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
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * Represents a single log entry.
 */
public class PrivilegeLogEntry implements Serializable, Comparable<PrivilegeLogEntry> {
	
	private static final long serialVersionUID = 1L;
	
	private final Integer userId;
	
	private final String privilege;
	
	private final boolean required;
	
	private final boolean missing;
	
	private final String whereChecked;
	
	private final Date date;
	
	public PrivilegeLogEntry(Integer userId, String privilege, boolean required, boolean missing, String whereChecked) {
		this.userId = userId;
		this.privilege = privilege;
		this.required = required;
		this.missing = missing;
		this.whereChecked = whereChecked;
		this.date = new Date();
	}
	
	public PrivilegeLogEntry(Integer userId, String privilege, boolean required, boolean missing) {
		this.userId = userId;
		this.privilege = privilege;
		this.required = required;
		this.missing = missing;
		this.whereChecked = null;
		this.date = null;
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
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	
	/**
	 * @return the missing
	 */
	public boolean isMissing() {
		return missing;
	}
	
	/**
	 * @return the whereChecked
	 */
	public String getWhereChecked() {
		return whereChecked;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + ((privilege == null) ? 0 : privilege.hashCode());
		result = prime * result + (required ? 1231 : 1237);
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
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (missing != other.missing)
			return false;
		if (privilege == null) {
			if (other.privilege != null)
				return false;
		} else if (!privilege.equals(other.privilege))
			return false;
		if (required != other.required)
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
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PrivilegeLogEntry o) {
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new Comparator<PrivilegeLogEntry>() {
			
			@Override
			public int compare(PrivilegeLogEntry o1, PrivilegeLogEntry o2) {
				if (o1.required && o2.required) {
					return 0;
				} else if (o1.required) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		chain.addComparator(new Comparator<PrivilegeLogEntry>() {
			
			@Override
			public int compare(PrivilegeLogEntry o1, PrivilegeLogEntry o2) {
				if (o1.missing && o2.missing) {
					return 0;
				} else if (o1.missing) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		chain.addComparator(new Comparator<PrivilegeLogEntry>() {
			
			@Override
			public int compare(PrivilegeLogEntry o1, PrivilegeLogEntry o2) {
				return o1.getPrivilege().compareTo(o2.getPrivilege());
			}
		});
		
		return chain.compare(o, this);
	}
	
}

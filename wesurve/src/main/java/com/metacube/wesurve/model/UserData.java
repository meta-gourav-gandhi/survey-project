/**
 * Used for retrieving current user for authorisation.
 */
package com.metacube.wesurve.model;

import com.metacube.wesurve.enums.Role;

public class UserData {
	private Role role;
	private int userId;
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
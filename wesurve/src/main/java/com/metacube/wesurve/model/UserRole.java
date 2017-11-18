/**
 * The UserRole class is POJO class for user_roles table.
 */
package com.metacube.wesurve.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRole {

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;

	@Column(name = "role", length = 20, nullable = false)
	private String role;

	public UserRole() {

	}

	public UserRole(int roleId, String role) {
		setRoleId(roleId);
		setRole(role);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
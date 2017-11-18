/**
 * DTO class
 */
package com.metacube.wesurve.dto;

public class LoginResponseDto {
	private int id;
	private String accessToken;
	private int role;
	private boolean viewer;
	private String name;
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public boolean isViewer() {
		return viewer;
	}

	public void setViewer(boolean viewer) {
		this.viewer = viewer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
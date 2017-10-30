package com.metacube.wesurve.dto;

public class LoginResponseDto {
	private int status;
	private String message;
	private String accessToken;
	private int role;
	private boolean viewer;
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
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
}
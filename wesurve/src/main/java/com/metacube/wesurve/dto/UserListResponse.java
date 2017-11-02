package com.metacube.wesurve.dto;

public class UserListResponse {
	boolean access;
	Iterable<UserDetailsDto> usersList;
	
	public boolean isAccess() {
		return access;
	}
	
	public void setAccess(boolean access) {
		this.access = access;
	}
	
	public Iterable<UserDetailsDto> getUsersList() {
		return usersList;
	}
	
	public void setUsersList(Iterable<UserDetailsDto> usersList) {
		this.usersList = usersList;
	}
}
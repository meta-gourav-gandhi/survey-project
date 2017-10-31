package com.metacube.wesurve.service;

import com.metacube.wesurve.model.User;

public interface UserService {
	public User createNewUser(User user);
	public boolean checkIfEmailExists(String email);
	public User checkAuthentication(String email, String password);
	public boolean isUserAViewer(String email);
	public void setAccessToken(User user, String accessToken);
}
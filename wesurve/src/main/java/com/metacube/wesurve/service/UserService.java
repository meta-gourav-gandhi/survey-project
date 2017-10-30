package com.metacube.wesurve.service;

import com.metacube.wesurve.model.User;

public interface UserService {
	public User createNewUser(User user);
	public boolean checkIfEmailExists(String email);
}
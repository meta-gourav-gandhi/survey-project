package com.metacube.wesurve.service;

import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;

public interface UserService {
	User createNewUser(User user);
	boolean checkIfEmailExists(String email);
	User checkAuthentication(String email, String password);
	boolean isUserAViewer(String email);
	Status setAccessToken(User user, String accessToken);
	User getUserByMail(String email);
	User getCustomUserByMail(String email);
	Status changePassword(User user, String newPassword);
	Iterable<User> getAllUsers();
	Role checkAuthorization(String accessToken);
	User getUserByAccessToken(String accessToken);
	User getUserById(int primaryKey);
	Status update(User user);
}
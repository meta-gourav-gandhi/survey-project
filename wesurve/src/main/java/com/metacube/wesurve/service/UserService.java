package com.metacube.wesurve.service;

import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;

public interface UserService {
	public User createNewUser(User user);
	public boolean checkIfEmailExists(String email);
	public User checkAuthentication(String email, String password);
	public boolean isUserAViewer(String email);
	public Status setAccessToken(User user, String accessToken);
	public User getUserByMail(String email);
	public User getCustomUserByMail(String email);
	public Status changePassword(User user, String newPassword);
	public Iterable<User> getAllUsers();
	public Role checkAuthorization(String accessToken);
	public User getUserByAccessToken(String accessToken);
	public User getUserById(int primaryKey);
	public Status update(User user);
}
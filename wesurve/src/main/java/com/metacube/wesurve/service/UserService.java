/**
 * The QuestionsService interface is service layer interface Questions Model 
 */
package com.metacube.wesurve.service;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserRole;

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
	UserData checkAuthorization(String accessToken);
	User getUserById(int primaryKey);
	Status update(User user);
	UserRole getUserRoleById(int primaryKey);
}
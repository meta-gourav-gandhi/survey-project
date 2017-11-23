/**
 * The UserDao is a DAO interface for the User Model class.
 */
package com.metacube.wesurve.dao;

import com.metacube.wesurve.model.User;

public interface UserDao extends AbstractDao<User, Integer> {
	boolean checkIfEmailExists(String email);
	User authenticateUser(String email, String password);
	User getUserByEmail(String email);
	User getUserByAccessToken(String accessToken);
}
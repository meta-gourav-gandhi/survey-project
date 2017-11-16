package com.metacube.wesurve.dao.userdetails;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.User;

public interface UserDao extends AbstractDao<User, Integer> {
	boolean checkIfEmailExists(String email);
	User authenticateUser(String email, String password);
	User getUserByEmail(String email);
	User getUserByAccessToken(String accessToken);
}
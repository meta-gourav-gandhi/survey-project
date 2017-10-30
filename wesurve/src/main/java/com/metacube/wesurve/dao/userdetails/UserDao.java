package com.metacube.wesurve.dao.userdetails;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.User;

public interface UserDao extends AbstractDao<User, Integer> {
	public boolean checkIfEmailExists(String email);
}
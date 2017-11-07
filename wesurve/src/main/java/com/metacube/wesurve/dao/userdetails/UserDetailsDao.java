package com.metacube.wesurve.dao.userdetails;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.UserDetails;

public interface UserDetailsDao extends AbstractDao<UserDetails, Integer> {
	public boolean checkIfEmailExists(String email);
}
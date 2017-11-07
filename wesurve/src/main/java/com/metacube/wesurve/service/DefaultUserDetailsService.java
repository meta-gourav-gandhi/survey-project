package com.metacube.wesurve.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.userdetails.UserDetailsDao;
import com.metacube.wesurve.model.UserDetails;

@Service("userDetailsService")
@Transactional
public class DefaultUserDetailsService implements UserDetailsService {

	@Resource(name = "hibernateUserDetailsDao")
	UserDetailsDao userDetailsDao;
	
	@Override
	public UserDetails createNewUser(UserDetails userDetails) {
		UserDetails userDetailsOutput = null;
		if(!userDetailsDao.checkIfEmailExists(userDetails.getEmail())) {
			userDetailsOutput = userDetailsDao.save(userDetails);
		}
		
		return userDetailsOutput;
	}
}
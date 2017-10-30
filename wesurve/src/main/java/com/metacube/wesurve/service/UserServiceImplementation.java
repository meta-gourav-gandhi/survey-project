package com.metacube.wesurve.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.userdetails.UserDao;
import com.metacube.wesurve.model.User;

@Service("userService")
@Transactional
public class UserServiceImplementation implements UserService {

	@Resource(name = "hibernateUserDaoImplementation")
	UserDao userDao;
	
	@Override
	public User createNewUser(User user) {
		User userOutput = null;
		if(!userDao.checkIfEmailExists(user.getEmail())) {
			userOutput = userDao.save(user);
		}
		
		return userOutput;
	}
}
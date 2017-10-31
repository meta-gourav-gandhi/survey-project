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
		return userDao.save(user);
	}

	@Override
	public boolean checkIfEmailExists(String email) {
		return userDao.checkIfEmailExists(email);
	}

	@Override
	public User checkAuthentication(String email, String password) {
		return userDao.authenticateUser(email, password);
	}

	@Override
	public boolean isUserAViewer(String email) {
		return false;
	}

	@Override
	public void setAccessToken(User user, String accessToken) {
		user.setToken(accessToken);
		userDao.update(user);
	}
}
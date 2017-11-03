package com.metacube.wesurve.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.userdetails.UserDao;
import com.metacube.wesurve.enums.Role;
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
		boolean result = false;
		User user = userDao.getUserByEmail(email);
		if (user.getSurveyListToView().size() != 0) {
			result = true;
		}

		return result;
	}

	@Override
	public void setAccessToken(User user, String accessToken) {
		user.setToken(accessToken);
		userDao.update(user);
	}

	@Override
	public User getUserByMail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public User getCustomUserByMail(String email) {
		User user = getUserByMail(email);
		if (user != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
			user = null;
		}

		return user;
	}

	@Override
	public void changePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		userDao.update(user);
	}

	@Override
	public Iterable<User> getAllUsers() {
		return userDao.findAll();
	}

	@Override
	public Role checkAuthorization(String accessToken) {
		User user = userDao.getUserByAccessToken(accessToken);
		Role role = null;
		try {
			int roleId = user.getUserRole().getRoleId();
			switch (roleId) {
			case 1:
				role = Role.ADMIN;
				break;
			case 2:
				role = Role.SURVEYOR;
				break;
			case 3:
				role = Role.USER;
				break;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			role = Role.INVALID;
		}

		return role;
	}

	@Override
	public User getUserByAccessToken(String accessToken) {
		return userDao.getUserByAccessToken(accessToken);
	}

	@Override
	public User getUserById(int primaryKey) {
		return userDao.findOne(primaryKey);
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}
}
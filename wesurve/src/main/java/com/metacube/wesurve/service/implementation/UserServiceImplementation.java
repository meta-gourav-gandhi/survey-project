package com.metacube.wesurve.service.implementation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.userdetails.UserDao;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserRole;
import com.metacube.wesurve.service.UserService;

@Service("userService")
public class UserServiceImplementation implements UserService {

	@Resource(name = "hibernateUserDaoImplementation")
	UserDao userDao;

	@Override
	public User createNewUser(User user) {
		User newUser;
		try {
			UserRole userRole = new UserRole();
			userRole.setRoleId(3);
			user.setUserRole(userRole);
			newUser = userDao.save(user);
		} catch (Exception exception) {
			newUser = null;
			exception.printStackTrace();
		}

		return newUser;
	}

	@Override
	public boolean checkIfEmailExists(String email) {
		boolean result;
		try {
			result = userDao.checkIfEmailExists(email);
		} catch (Exception exception) {
			result = false;
			exception.printStackTrace();
		}

		return result;
	}

	@Override
	public User checkAuthentication(String email, String password) {
		User user;
		try {
			user = userDao.authenticateUser(email, password);
		} catch (Exception exception) {
			user = null;
		}

		return user;
	}

	@Override
	public boolean isUserAViewer(String email) {
		boolean result = false;
		try {
			User user = userDao.getUserByEmail(email);
			if (user.getSurveyListToView().size() != 0) {
				result = true;
			}
		} catch (Exception exception) {
			result = false;
			exception.printStackTrace();
		}

		return result;
	}

	@Override
	public Status setAccessToken(User user, String accessToken) {
		Status status;
		try {
			user.setToken(accessToken);
			userDao.update(user);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
		}

		return status;
	}

	@Override
	public User getUserByMail(String email) {
		User user;
		try {
			user = userDao.getUserByEmail(email);
		} catch (Exception exception) {
			user = null;
			exception.printStackTrace();
		}

		return user;
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
	public Status changePassword(User user, String newPassword) {
		Status status;
		try {
			user.setPassword(newPassword);
			userDao.update(user);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
			exception.printStackTrace();
		}

		return status;

	}

	@Override
	public Iterable<User> getAllUsers() {
		Iterable<User> iterableOfUsers;
		try {
			iterableOfUsers = userDao.findAll();
		} catch (Exception excpetion) {
			iterableOfUsers = null;
			excpetion.printStackTrace();
		}

		return iterableOfUsers;
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
		User user;
		try {
			user = userDao.getUserByAccessToken(accessToken);
		} catch (Exception exception) {
			user = null;
			exception.printStackTrace();
		}

		return user;
	}

	@Override
	public User getUserById(int primaryKey) {
		User user;
		try {
			user = userDao.findOne(primaryKey);
		} catch (Exception exception) {
			user = null;
			exception.printStackTrace();
		}

		return user;
	}

	@Override
	public Status update(User user) {
		Status status;
		try {
			userDao.update(user);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
			exception.printStackTrace();
		}

		return status;
	}
}
/**
 * The UserServiceImplementation class is service class for Users Model.
 */
package com.metacube.wesurve.service.implementation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.UserDao;
import com.metacube.wesurve.dao.UserRoleDao;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserData;
import com.metacube.wesurve.model.UserRole;
import com.metacube.wesurve.service.UserService;

@Service("userService")
public class UserServiceImplementation implements UserService {

	@Resource(name = "hibernateUserDaoImplementation")
	UserDao userDao;
	
	@Resource(name = "hibernateUserRoleDaoImplementation")
	UserRoleDao userRoleDao;

	/**
	 * This method creates new user.
	 * @param user - User object
	 * @return User - created User
	 */
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

	/**
	 * This method checks if email id exists.
	 * @param email
	 * @return boolean
	 */
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

	/**
	 * This method checks authentication of the user.
	 * @param email
	 * @param password
	 * @return User object
	 */
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

	/**
	 * This method checks if a user is a viewer of any survey result or not.
	 * @param email
	 * @return boolean
	 */
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

	/**
	 * This method sets the access token of an user.
	 * @param user
	 * @param accessToken
	 * @return Status
	 */
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

	/**
	 * This method return the User object with the given mail id.
	 * @param email
	 * @return User object
	 */
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

	/**
	 * This method returns the User object with the given mail id 
	 * who hasn't logged in using social login
	 * @param email
	 * @return User object
	 */
	@Override
	public User getCustomUserByMail(String email) {
		User user = getUserByMail(email);
		if (user != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
			user = null;
		}

		return user;
	}

	/**
	 * This method changes password of the user.
	 * @param user
	 * @param newPassword
	 * @return Status
	 */
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

	/**
	 * This method returns list of all users.
	 * @return Iterable<User>
	 */
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

	/**
	 * This method checks authorisation of the user.
	 * @param accessToken
	 * @return UserData object containing access token and user id.
	 */
	@Override
	public UserData checkAuthorization(String accessToken) {
		UserData userData = new UserData();
		
		User user = userDao.getUserByAccessToken(accessToken);
		int userId;
		Role role;
		try {
			int roleId = user.getUserRole().getRoleId();
			userId = user.getUserId();
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
			default:
				role = Role.USER;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			role = Role.INVALID;
			userId = 0;
		}
		
		userData.setRole(role);
		userData.setUserId(userId);
		return userData;
	}

	/**
	 * This method returns User object with the given user id.
	 * @param primaryKey
	 * @return User object
	 */
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

	/**
	 * This method updates the user in database.
	 * @param user - User object
	 * @return Status
	 */
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

	/**
	 * This method returns the role of user using its role id.
	 * @param primaryKey
	 * @return UserRole object
	 */
	@Override
	public UserRole getUserRoleById(int primaryKey) {
		UserRole userRole;
		try {
			userRole = userRoleDao.findOne(primaryKey);
		} catch(Exception exception) {
			exception.printStackTrace();
			userRole = null;
		}
		
		return userRole;
	}
}
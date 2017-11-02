package com.metacube.wesurve.facade;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.UserService;
import com.metacube.wesurve.utils.EmailUtils;
import com.metacube.wesurve.utils.MD5Encryption;
import com.metacube.wesurve.utils.StringUtils;

@Component("userFacade")
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	public UserFacadeImplementation() {
	}

	/**
	 * @param userDto
	 *            details of the user to save in the database
	 * @return return the success , failure,success,duplicate
	 */
	@Override
	public Status createNewUser(UserDto userDto) {
		Status outputStatus = Status.FAILURE;
		if (validateUser(userDto)) {
			if (!userService.checkIfEmailExists(userDto.getEmail())) {
				User user = convertDtoToModel(userDto);
				if (userService.createNewUser(user) != null) {
					outputStatus = Status.SUCCESS;
				}
			} else {
				outputStatus = Status.DUPLICATE;
			}
		}
		return outputStatus;
	}

	/**
	 * @param userDto
	 *            to validate the UserDto
	 * @return boolean function to validate user if its credentials are correct
	 */
	private boolean validateUser(UserDto userDto) {
		boolean result = true;

		boolean condition1 = StringUtils.validateString(userDto.getName())
				|| StringUtils.validateString(userDto.getPassword()) || StringUtils.validateString(userDto.getEmail())
				|| (userDto.getDob() == null);
		boolean condition2 = userDto.getPassword().length() >= 8;
		boolean condition3 = StringUtils.validateEmail(userDto.getEmail());

		if (condition1 == false || condition2 == false || condition3 == false) {
			result = false;
		}

		return result;
	}

	private User convertDtoToModel(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getName());
		SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = targetFormat.parse(userDto.getDob());
		} catch (ParseException e) {
			try {
				date = targetFormat.parse(targetFormat.format(sourceFormat.parse(userDto.getDob())));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		user.setDob(date);
		user.setEmail(userDto.getEmail());
		user.setGender(userDto.getGender().charAt(0));
		try {
			user.setPassword(MD5Encryption.encrypt(userDto.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		return user;
	}

	public LoginResponseDto login(LoginCredentialsDto loginCredentialsDto) {
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		User user = authenticate(loginCredentialsDto);
		if (user != null) {
			loginResponseDto.setStatus(200);
			loginResponseDto.setMessage("Login Successful");
			loginResponseDto.setName(user.getName());
			loginResponseDto.setEmail(user.getEmail());
			loginResponseDto.setRole(user.getUserRole().getRoleId());
			loginResponseDto.setViewer(userService.isUserAViewer(user.getEmail()));
			String accessToken = getAccessTokenIfUserLoggedIn(user);

			if (accessToken == null || accessToken.isEmpty()) {
				String newAccessToken = StringUtils.generateAccessToken(user.getEmail());
				loginResponseDto.setAccessToken(newAccessToken);
				userService.setAccessToken(user, newAccessToken);
			} else {
				loginResponseDto.setAccessToken(accessToken);
			}
		} else {
			loginResponseDto.setStatus(400);
			loginResponseDto.setMessage("Error in login");
		}

		return loginResponseDto;
	}

	private String getAccessTokenIfUserLoggedIn(User user) {
		return user.getToken();
	}

	private User authenticate(LoginCredentialsDto loginCredentialsDto) {
		User user = null;
		System.out.println(loginCredentialsDto.getPassword());
		if (StringUtils.validateEmail(loginCredentialsDto.getEmail())
				|| StringUtils.validateString(loginCredentialsDto.getPassword())
				|| loginCredentialsDto.getPassword().length() >= 8) {
			try {
				user = userService.checkAuthentication(loginCredentialsDto.getEmail(),
						MD5Encryption.encrypt(loginCredentialsDto.getPassword()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	@Override
	public LoginResponseDto socialLogin(UserDto socialLoginCredentials) {
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if (validateSocialLoginCredentials(socialLoginCredentials)) {
			User user = createUserIfNotExists(socialLoginCredentials);
			if (user != null) {
				loginResponseDto.setStatus(200);
				loginResponseDto.setMessage("Login Successful");
				loginResponseDto.setName(user.getName());
				loginResponseDto.setEmail(user.getEmail());
				loginResponseDto.setRole(user.getUserRole().getRoleId());
				loginResponseDto.setViewer(userService.isUserAViewer(user.getEmail()));
				String accessToken = getAccessTokenIfUserLoggedIn(user);

				if (accessToken == null || accessToken.isEmpty()) {
					String newAccessToken = StringUtils.generateAccessToken(user.getEmail());
					loginResponseDto.setAccessToken(newAccessToken);
					userService.setAccessToken(user, newAccessToken);
				} else {
					loginResponseDto.setAccessToken(accessToken);
				}
			}
		} else {
			loginResponseDto.setStatus(400);
			loginResponseDto.setMessage("Error in login");
		}

		return loginResponseDto;
	}

	private User createUserIfNotExists(UserDto socialUserDto) {
		User user;

		if (!userService.checkIfEmailExists(socialUserDto.getEmail())) {
			user = userService.createNewUser(convertDtoToModel(socialUserDto));
		} else {
			user = userService.getUserByMail(socialUserDto.getEmail());
		}

		return user;
	}

	private boolean validateSocialLoginCredentials(UserDto socialLoginCredentials) {
		return StringUtils.validateEmail(socialLoginCredentials.getEmail())
				&& StringUtils.validateString(socialLoginCredentials.getName());
	}

	@Override
	public Status forgotPassword(String email) {
		Status status = Status.FAILURE;
		if (StringUtils.validateEmail(email)) {
			User user = userService.getCustomUserByMail(email);
			if (user != null) {
				String newPassword = StringUtils.randomAlphanumeric(10);
				try {
					userService.changePassword(user, MD5Encryption.encrypt(newPassword));
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
				String emailBody = "Hello " + user.getName() + ",\nYour new password is: \n" + newPassword
						+ "\n\nYou can use this password for login and you can change the password from the user profile.\n\nThanks & Regards,\nWeServe Helpline";
				try {
					EmailUtils.sendEmail("wesurvehelpline@gmail.com", "wesurve#123", email, "Recover Password",
							emailBody);
					status = Status.SUCCESS;
				} catch (MessagingException e) {
					e.printStackTrace();
					status = Status.FAILURE;
				}

			}
		}

		return status;
	}

	@Override
	public Iterable<UserDetailsDto> getAllUsers(String accessToken) {
		List<UserDetailsDto> userDetailsDtoList = new ArrayList<>();
		for (User user : userService.getAllUsers()) {
			if (user.getToken() != null && user.getToken().equals(accessToken)) {
				continue;
			}
			userDetailsDtoList.add(modelToDto(user));
		}

		return userDetailsDtoList;
	}

	private UserDetailsDto modelToDto(User user) {
		if (user == null) {
			return null;
		} else {
			UserDetailsDto curUserDetails = new UserDetailsDto();
			curUserDetails.setEmail(user.getEmail());
			curUserDetails.setName(user.getName());
			curUserDetails.setRole(user.getUserRole().getRoleId());
			return curUserDetails;
		}
	}

	@Override
	public Status logout(String accessToken) {
		User user = userService.getUserByAccessToken(accessToken);
		Status status = null;
		try {
			userService.setAccessToken(user, "");
			status = Status.SUCCESS;
		} catch (Exception exception) {
			exception.printStackTrace();
			status = Status.FAILURE;
		}

		return status;
	}

	@Override
	public Role checkAuthorization(String accessToken) {
		return userService.checkAuthorization(accessToken);
	}

	@Override
	public Status changeUserRole(String token, String email) {
		Status status = Status.FAILURE;
		User user = userService.getUserByMail(email);
		if (user != null) {
			if (user.getUserRole().getRoleId() == 2) {
				user.getUserRole().setRoleId(3);
			} else if (user.getUserRole().getRoleId() == 3) {
				user.getUserRole().setRoleId(2);
			}
			status = Status.SUCCESS;
		}

		return status;
	}
}
package com.metacube.wesurve.facade;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.UserService;
import com.metacube.wesurve.utils.PasswordEncryption;

@Component("userFacade")
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	public UserFacadeImplementation() {
	}

	/**
	 * @param userDto details of the user to save in the database
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
	 * @param userDto to validate the  UserDto 
	 * @return boolean
	 * function to validate user if its credentials are correct
	 */
	private boolean validateUser(UserDto userDto) {
		boolean result = true;

		boolean condition1 = validateString(userDto.getName())
				|| validateString(userDto.getPassword())
				|| validateString(userDto.getEmail())
				|| (userDto.getDob() == null);
		boolean condition2 = userDto.getPassword().length() >= 8;
		boolean condition3 = validateEmail(userDto.getEmail());

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
		user.setPassword(PasswordEncryption.encrypt(userDto.getPassword()));
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		return user;
	}

	private boolean validateString(String string) {
		return !(string == null || string.trim().isEmpty());
	}

	/**
	 * @param email to to check if the email is in proper format
	 * @return boolean
	 * function to validate the email of the user
	 */
	private boolean validateEmail(String email) {
		boolean result = false;
		if (validateString(email)) {
			Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
			Matcher matcher = validEmailPattern.matcher(email);
			result = matcher.find();
		}

		return result;
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
			loginResponseDto.setViewer(userService.isUserAViewer(loginCredentialsDto.getEmail()));
			
			String accessToken = generateAccessToken(loginCredentialsDto.getEmail());
			loginResponseDto.setAccessToken(accessToken);
			userService.setAccessToken(user, accessToken);
		} else {
			loginResponseDto.setStatus(400);
			loginResponseDto.setMessage("Error in login");
		}

		return loginResponseDto;
	}

	/**
	 * @param email to append in the access token
	 * @return the random access token when user login 
	 * function to generate the access token 
	 */
	private String generateAccessToken(String email) {
		SecureRandom random = new SecureRandom();
		long longToken = Math.abs(random.nextLong());
		String token = Long.toString(longToken, 16);
		token += email;
		return token;
	}

	private User authenticate(LoginCredentialsDto loginCredentialsDto) {
		User user = null;
		System.out.println(loginCredentialsDto.getPassword());
		if (validateEmail(loginCredentialsDto.getEmail()) || validateString(loginCredentialsDto.getPassword()) || loginCredentialsDto.getPassword().length() >= 8) {
			user = userService.checkAuthentication(loginCredentialsDto.getEmail(), PasswordEncryption.encrypt(loginCredentialsDto.getPassword()));
		}
		
		return user;
	}

	@Override
	public LoginResponseDto socialLogin(UserDto socialLoginCredentials) {
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if(validateSocialLoginCredentials(socialLoginCredentials)) {
			
			if (!userService.checkIfEmailExists(socialLoginCredentials.getEmail())) {
				User user = convertDtoToModel(socialLoginCredentials);
				userService.createNewUser(user);
			}
			
			User user = userService.getUserByMail(socialLoginCredentials.getEmail());
			loginResponseDto.setStatus(200);
			loginResponseDto.setMessage("Login Successful");
			loginResponseDto.setName(socialLoginCredentials.getName());
			loginResponseDto.setEmail(socialLoginCredentials.getEmail());
			loginResponseDto.setRole(user.getUserRole().getRoleId());
			String accessToken = generateAccessToken(socialLoginCredentials.getEmail());
			loginResponseDto.setAccessToken(accessToken);
			userService.setAccessToken(user, accessToken);
			loginResponseDto.setViewer(userService.isUserAViewer(socialLoginCredentials.getEmail()));
		} else {
			loginResponseDto.setStatus(400);
			loginResponseDto.setMessage("Error in login");
		}
		
		return loginResponseDto;
	}

	private boolean validateSocialLoginCredentials(UserDto socialLoginCredentials) {
		return validateEmail(socialLoginCredentials.getEmail())
				&& validateString(socialLoginCredentials.getName());
	}

	@Override
	public Status forgotPassword(String email) {
		
		return null;
	}
}
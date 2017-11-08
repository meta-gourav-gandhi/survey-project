package com.metacube.wesurve.facade;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.SurveyService;
import com.metacube.wesurve.service.UserService;
import com.metacube.wesurve.utils.EmailUtils;
import com.metacube.wesurve.utils.MD5Encryption;
import com.metacube.wesurve.utils.StringUtils;

@Component("userFacade")
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	@Autowired
	SurveyService surveyService;

	public UserFacadeImplementation() {
	}

	/**
	 * @param userDto
	 *            details of the user to save in the database
	 * @return return the success, failure, success, duplicate
	 */
	@Override
	public ResponseDto<Void> createNewUser(UserDto userDto) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.INVALID_CONTENT;
		if (validateUser(userDto)) {
			if (!userService.checkIfEmailExists(userDto.getEmail())) {
				userService.createNewUser(convertDtoToModel(userDto));
				status = Status.SUCCESS;
			} else {
				status = Status.DUPLICATE;
			}
		}

		response.setStatus(status);
		return response;
	}

	/**
	 * @param userDto
	 *            to validate the UserDto
	 * @return boolean function to validate user if its credentials are correct
	 */
	private boolean validateUser(UserDto userDto) {
		return (StringUtils.validateString(userDto.getName()) && userDto.getName().trim().length() >= 2)
				&& (StringUtils.validateString(userDto.getPassword()) && userDto.getPassword().length() >= 8)
				&& (StringUtils.validateEmail(userDto.getEmail())) && (userDto.getDob() != null);
	}

	private User convertDtoToModel(UserDto userDto) {
		User user = new User();

		user.setName(userDto.getName());

		SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		String dob = userDto.getDob();
		try {
			if (StringUtils.validateString(dob)) {
				date = targetFormat.parse(userDto.getDob());
			}
		} catch (ParseException e) {
			try {
				if (StringUtils.validateString(dob)) {
					date = targetFormat.parse(targetFormat.format(sourceFormat.parse(userDto.getDob())));
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		user.setDob(date);
		user.setEmail(userDto.getEmail());
		String gender = userDto.getGender();
		if(StringUtils.validateString(gender)) {
			user.setGender(gender.charAt(0));
		}
		

		try {
			user.setPassword(MD5Encryption.encrypt(userDto.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		return user;
	}

	public ResponseDto<LoginResponseDto> login(LoginCredentialsDto loginCredentialsDto) {
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();

		LoginResponseDto loginResponseDto = null;
		Status status;

		User user = authenticate(loginCredentialsDto);
		if (user != null) {
			status = Status.SUCCESS;

			loginResponseDto = new LoginResponseDto();
			loginResponseDto.setName(user.getName());
			loginResponseDto.setEmail(user.getEmail());
			loginResponseDto.setRole(user.getUserRole().getRoleId());
			loginResponseDto.setViewer(userService.isUserAViewer(user.getEmail()));
			String accessToken = getAccessTokenIfUserLoggedIn(user);

			if (accessToken == null || accessToken.isEmpty()) {
				String newAccessToken = null;
				try {
					newAccessToken = StringUtils.generateAccessToken(user.getEmail());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				loginResponseDto.setAccessToken(newAccessToken);
				userService.setAccessToken(user, newAccessToken);
			} else {
				loginResponseDto.setAccessToken(accessToken);
			}
		} else {
			status = Status.INVALID;
		}

		response.setStatus(status);
		response.setBody(loginResponseDto);
		return response;
	}

	private String getAccessTokenIfUserLoggedIn(User user) {
		return user.getToken();
	}

	/**
	 * @param loginCredentialsDto
	 *            email and password to authenticate user
	 * @return user
	 */
	private User authenticate(LoginCredentialsDto loginCredentialsDto) {
		User user = null;
		if (StringUtils.validateEmail(loginCredentialsDto.getEmail())
				&& StringUtils.validateString(loginCredentialsDto.getPassword())
				&& loginCredentialsDto.getPassword().length() >= 8) {

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
	public ResponseDto<LoginResponseDto> socialLogin(UserDto socialLoginCredentials) {
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		LoginResponseDto loginResponseDto = null;
		Status status = null;
		if (validateSocialLoginCredentials(socialLoginCredentials)) {
			User user = createUserIfNotExists(socialLoginCredentials);
			if (user != null) {
				status = Status.SUCCESS;

				loginResponseDto = new LoginResponseDto();
				loginResponseDto.setId(user.getUserId());
				loginResponseDto.setName(user.getName());
				loginResponseDto.setEmail(user.getEmail());
				loginResponseDto.setRole(user.getUserRole().getRoleId());
				loginResponseDto.setViewer(userService.isUserAViewer(user.getEmail()));
				String accessToken = getAccessTokenIfUserLoggedIn(user);

				if (accessToken == null || accessToken.isEmpty()) {
					String newAccessToken = null;
					try {
						newAccessToken = StringUtils.generateAccessToken(user.getEmail());
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}

					loginResponseDto.setAccessToken(newAccessToken);
					userService.setAccessToken(user, newAccessToken);
				} else {
					loginResponseDto.setAccessToken(accessToken);
				}
			}
		} else {
			status = Status.INVALID;
		}

		response.setStatus(status);
		response.setBody(loginResponseDto);

		return response;
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
	public ResponseDto<Void> forgotPassword(String email) {
		ResponseDto<Void> response = new ResponseDto<>();
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
		response.setStatus(status);
		return response;
	}

	@Override
	public Iterable<UserDetailsDto> getAllUsers(String accessToken) {
		Set<UserDetailsDto> userDetailsDtoSet = new HashSet<>();
		for (User user : userService.getAllUsers()) {
			if (user.getToken() != null && user.getToken().equals(accessToken)) {
				continue;
			}
			
			userDetailsDtoSet.add(modelToDto(user));
		}

		return userDetailsDtoSet;
	}

	private UserDetailsDto modelToDto(User user) {
		if (user == null) {
			return null;
		} else {
			UserDetailsDto curUserDetails = new UserDetailsDto();
			curUserDetails.setId(user.getUserId());
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

	/**
	 * @param userId
	 *            id of the user whose role needs to changed
	 * @return Status
	 */
	@Override
	public Status changeUserRole(int userId) {
		Status status = Status.FAILURE;
		User user = userService.getUserById(userId);
		if (user != null) {
			if (user.getUserRole().getRoleId() == 2) {
				user.getUserRole().setRoleId(3);
				Set<Survey> createdSurveys = user.getCreatedSurveyList();
				for (Survey survey : createdSurveys) {
					surveyService.changeSurveyStatus(survey, SurveyStatus.NOTLIVE);
				}
			} else if (user.getUserRole().getRoleId() == 3) {
				user.getUserRole().setRoleId(2);
			}

			userService.update(user);
			status = Status.SUCCESS;
		}

		return status;
	}

	@Override
	public Iterable<SurveyInfoDto> getSurveyList(String token) {
		User user = userService.getUserByAccessToken(token);
		Set<SurveyInfoDto> surveyInfoList = new HashSet<>();
		if(user != null && user.getSurveyListToView().size() != 0) {
			Set<Survey> surveys = user.getSurveyListToView();
			for(Survey curSurvey : surveys) {
				surveyInfoList.add(convertModeltoDtoSurvey(curSurvey));
			}
		}
		
		return surveyInfoList;
	}

	private SurveyInfoDto convertModeltoDtoSurvey(Survey curSurvey) {
		SurveyInfoDto surveyInfoDtoObject = new SurveyInfoDto();
		surveyInfoDtoObject.setId(curSurvey.getSurveyId());
		surveyInfoDtoObject.setDescription(curSurvey.getDescription());
		surveyInfoDtoObject.setSurveyName(curSurvey.getSurveyName());
		
		return surveyInfoDtoObject;
	}

	@Override
	public ResponseDto<Void> changePassword(String accessToken, String currentPassword, String newPassword) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.FAILURE;
		User user = userService.getUserByAccessToken(accessToken);
		
		try {
			if (currentPassword.equals(newPassword)) {
				status = Status.DUPLICATE;
			} else if (userService.getCurrentPassword(accessToken).getPassword().equals(MD5Encryption.encrypt(currentPassword))) {
				user.setPassword(MD5Encryption.encrypt(newPassword));
				userService.update(user);
				status = Status.SUCCESS;
			}
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		
		response.setStatus(status);
		return response;
	}
}
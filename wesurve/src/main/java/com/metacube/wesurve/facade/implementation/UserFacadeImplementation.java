package com.metacube.wesurve.facade.implementation;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDetailsForSurveyorDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.facade.UserFacade;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserRole;
import com.metacube.wesurve.service.SurveyService;
import com.metacube.wesurve.service.UserService;
import com.metacube.wesurve.utils.EmailUtils;
import com.metacube.wesurve.utils.MD5Encryption;
import com.metacube.wesurve.utils.StringUtils;

@Component("userFacade")
@Transactional
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	@Autowired
	SurveyService surveyService;

	public UserFacadeImplementation() {
	}

	/**
	 * @param userDto details of the user to save in the database
	 * @return return the success, failure, success, duplicate
	 */
	@Override
	public ResponseDto<Void> createNewUser(UserDto userDto) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.INVALID_CONTENT;
		if (validateUser(userDto)) {
			if (!userService.checkIfEmailExists(userDto.getEmail())) {
				User newUser = userService.createNewUser(convertDtoToModel(userDto));
				if (newUser != null) {
					status = Status.SUCCESS;
				} else {
					status = Status.FAILURE;
				}
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
		if (StringUtils.validateString(gender)) {
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
					status = userService.setAccessToken(user, newAccessToken);
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
					status = userService.changePassword(user, MD5Encryption.encrypt(newPassword));
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
				if (status != Status.FAILURE) {
					String emailBody = "Hello " + user.getName() + ",\nYour new password is: \n" + newPassword
							+ "\n\nYou can use this password for login and you can change the password from the user profile.\n\nThanks & Regards,\nWeSurve Helpline";
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
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public Iterable<UserDetailsDto> getAllUsers(int userId) {
		Set<UserDetailsDto> userDetailsDtoSet = new HashSet<>();
		Iterable<User> iterableOfUsers = userService.getAllUsers();
		if (iterableOfUsers != null) {
			for (User user : iterableOfUsers) {
				if (user.getUserId() == userId) {
					continue;
				}

				userDetailsDtoSet.add(modelToDto(user));
			}
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
	public Status logout(int userId) {
		User user = userService.getUserById(userId);
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
	public UserData checkAuthorization(String accessToken) {
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
				UserRole role = userService.getUserRoleById(3);
				user.setUserRole(role);
				Set<Survey> createdSurveys = user.getCreatedSurveyList();
				
					for (Survey survey : createdSurveys) {
						surveyService.changeSurveyStatus(survey, SurveyStatus.NOTLIVE);
					}
				
			} else if (user.getUserRole().getRoleId() == 3) {
				UserRole role = userService.getUserRoleById(2);
				user.setUserRole(role);
			}

			status = userService.update(user);
		}

		return status;
	}

	@Override
	public Iterable<SurveyInfoDto> getSurveyListForViewers(int userId) {
		User user = userService.getUserById(userId);
		Set<SurveyInfoDto> surveyInfoList = new HashSet<>();
		if (user != null && user.getSurveyListToView().size() != 0) {
			Set<Survey> surveys = user.getSurveyListToView();
			for (Survey curSurvey : surveys) {
				if (curSurvey.getSurveyStatus() != SurveyStatus.DELETED) {
					surveyInfoList.add(convertModeltoDtoSurveyInfo(curSurvey));
				}
			}
		}

		return surveyInfoList;
	}

	private SurveyInfoDto convertModeltoDtoSurveyInfo(Survey curSurvey) {
		SurveyInfoDto surveyInfoDtoObject = new SurveyInfoDto();
		surveyInfoDtoObject.setId(curSurvey.getSurveyId());
		surveyInfoDtoObject.setDescription(curSurvey.getDescription());
		surveyInfoDtoObject.setSurveyName(curSurvey.getSurveyName());
		surveyInfoDtoObject.setStatus(curSurvey.getSurveyStatus());
		surveyInfoDtoObject.setSurveyUrl(surveyService.getSurveyURL(curSurvey));
		String labels = convertModelToDtoLabel(curSurvey.getLabels());
		if (labels.length() > 1) {
			labels = labels.substring(0, labels.length() - 1);
		}

		surveyInfoDtoObject.setLabels(labels);
		return surveyInfoDtoObject;
	}

	private String convertModelToDtoLabel(Set<Labels> labels) {
		String label = "";
		if (labels != null) {
			for (Labels currObj : labels) {
				label += currObj.getLabelName() + ",";
			}
		}

		return label;
	}

	@Override
	public ResponseDto<Void> changePassword(int userId, String currentPassword, String newPassword) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.FAILURE;
		User user = userService.getUserById(userId);

		try {
			if (StringUtils.validateString(newPassword) && StringUtils.validateString(currentPassword) && newPassword.length() >= 8) {
				if (currentPassword.equals(newPassword)) {
					status = Status.DUPLICATE;
				} else if (user != null && user.getPassword().equals(MD5Encryption.encrypt(currentPassword))) {
					user.setPassword(MD5Encryption.encrypt(newPassword));
					status = userService.update(user);

				}
			}
			else {
				status = Status.INVALID_CONTENT;
			}
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public Iterable<SurveyInfoDto> getSurveyListOfSurveyor(int userId) {
		User user = userService.getUserById(userId);
		Set<SurveyInfoDto> surveyInfoList = new HashSet<>();
		;
		Set<Survey> surveys = user.getCreatedSurveyList();
		for (Survey curSurvey : surveys) {
			if (curSurvey.getSurveyStatus() != SurveyStatus.DELETED) {
				surveyInfoList.add(convertModeltoDtoSurveyInfo(curSurvey));
			}
		}

		return surveyInfoList;
	}

	@Override
	public Iterable<SurveyInfoDto> getListOfFilledSurveys(int userId) {
		User user = userService.getUserById(userId);
		Set<SurveyInfoDto> filledSurveysInfo = new HashSet<>();
		Set<Survey> filledSurveys = user.getFilledSurveyList();
		for (Survey currentSurvey : filledSurveys) {
			if (currentSurvey.getSurveyStatus() != SurveyStatus.DELETED) {
				filledSurveysInfo.add(convertModeltoDtoSurveyInfo(currentSurvey));
			}
		}

		return filledSurveysInfo;
	}

	@Override
	public Iterable<UserDetailsForSurveyorDto> getAllUsersForSurveyor(int surveyorId, int surveyId) {
		Set<UserDetailsForSurveyorDto> userDetailsForSurveyorDtoSet = new HashSet<>();
		Iterable<User> iterableOfUsers = userService.getAllUsers();
		if (iterableOfUsers != null) {
			Survey surveyObj = surveyService.getSurveyById(surveyId);
			for (User user : iterableOfUsers) {
				if (user.getUserId() == surveyorId) {
					continue;
				}

				UserDetailsForSurveyorDto curObj = modelToDtoForSurveyor(user);
				if (surveyObj.getViewers().contains(user)) {
					curObj.setSurveyViewer(true);
				} else {
					curObj.setSurveyViewer(false);
				}

				userDetailsForSurveyorDtoSet.add(curObj);
			}
		}

		return userDetailsForSurveyorDtoSet;
	}

	private UserDetailsForSurveyorDto modelToDtoForSurveyor(User user) {
		UserDetailsForSurveyorDto curUser = new UserDetailsForSurveyorDto();
		curUser.setId(user.getUserId());
		curUser.setEmail(user.getEmail());
		curUser.setName(user.getName());
		return curUser;
	}
}
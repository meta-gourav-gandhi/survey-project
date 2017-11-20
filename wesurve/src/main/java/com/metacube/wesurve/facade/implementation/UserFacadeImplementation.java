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
	 * @param userDto to validate the UserDto
	 * @return boolean function to validate user if its credentials are correct
	 */
	private boolean validateUser(UserDto userDto) {
		return (StringUtils.validateString(userDto.getName()) 
					&& userDto.getName().trim().length() >= 2)
				&& (StringUtils.validateString(userDto.getPassword()) 
						&& userDto.getPassword().length() >= 8)
				&& (StringUtils.validateEmail(userDto.getEmail()))
				&& (userDto.getDob() != null);
	}

	/**
	 * This method converts UserDto to User Model object
	 * @param userDto
	 * @return User object
	 */
	private User convertDtoToModel(UserDto userDto) {
		User user = new User();

		user.setName(userDto.getName());

		//Setting date for different formats.
		String dob = userDto.getDob();
		if (StringUtils.validateString(dob)) { 
			SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			
			try {
				date = targetFormat.parse(dob);
			} catch (ParseException e) {
				try {
					date = targetFormat.parse(targetFormat.format(sourceFormat.parse(dob)));
				} catch (ParseException e1) {
					date = null;
					e1.printStackTrace();
				}
			}
			
			user.setDob(date);
		}

		
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

	/**
	 * This method logs in the user.
	 * @param loginCredentialsDto - contains login credentials
	 * @return ResponseDto<LoginResponseDto>
	 */
	@Override
	public ResponseDto<LoginResponseDto> login(LoginCredentialsDto loginCredentialsDto) {
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();

		LoginResponseDto loginResponseDto = null;
		Status status;

		// Authenticates the user.
		User user = authenticate(loginCredentialsDto);
		if (user != null) {
			loginResponseDto = generateLoginResponse(user);
			status = Status.SUCCESS;
		} else {
			status = Status.INVALID;
		}

		response.setStatus(status);
		response.setBody(loginResponseDto);
		return response;
	}

	/**
	 * @param loginCredentialsDto email and password to authenticate user
	 * @return user
	 */
	private User authenticate(LoginCredentialsDto loginCredentialsDto) {
		User user = null;
		Boolean condition = StringUtils.validateEmail(loginCredentialsDto.getEmail())
				&& StringUtils.validateString(loginCredentialsDto.getPassword())
				&& loginCredentialsDto.getPassword().length() >= 8;
		if (condition) {
			try {
				String email = loginCredentialsDto.getEmail();
				String password = MD5Encryption.encrypt(loginCredentialsDto.getPassword());
				user = userService.checkAuthentication(email, password);
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
				loginResponseDto = generateLoginResponse(user);
				status = Status.SUCCESS;
			}
		} else {
			status = Status.INVALID;
		}

		response.setStatus(status);
		response.setBody(loginResponseDto);

		return response;
	}

	/**
	 * This method generates LoginResponseDto object for the given User object
	 * @param user - User object
	 * @return LoginResponseDto object
	 */
	private LoginResponseDto generateLoginResponse(User user) {
		LoginResponseDto loginResponse = new LoginResponseDto();
		loginResponse.setId(user.getUserId());
		loginResponse.setName(user.getName());
		loginResponse.setEmail(user.getEmail());
		loginResponse.setRole(user.getUserRole().getRoleId());
		loginResponse.setViewer(userService.isUserAViewer(user.getEmail()));
		String accessToken = user.getToken();

		if (!StringUtils.validateString(accessToken)) {
			try {
				// Generates new Access Token
				accessToken = StringUtils.generateAccessToken(user.getEmail());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			userService.setAccessToken(user, accessToken);
		}
		
		loginResponse.setAccessToken(accessToken);
		return loginResponse;
	}

	/**
	 * For social login users this method creates new user if it does not already exists.
	 * @param socialUserDto - UsrDto object
	 * @return User object
	 */
	private User createUserIfNotExists(UserDto socialUserDto) {
		User user;

		if (!userService.checkIfEmailExists(socialUserDto.getEmail())) {
			user = userService.createNewUser(convertDtoToModel(socialUserDto));
		} else {
			user = userService.getUserByMail(socialUserDto.getEmail());
		}

		return user;
	}

	/**
	 * This method validates social login credentials
	 * @param socialLoginCredentials
	 * @return boolean
	 */
	private boolean validateSocialLoginCredentials(UserDto socialLoginCredentials) {
		return StringUtils.validateEmail(socialLoginCredentials.getEmail())
				&& StringUtils.validateString(socialLoginCredentials.getName());
	}

	/**
	 * This method is for forgot password option.
	 * It generates the new password and sends mail to the given mail id.
	 * @param email 
	 * @return ResponseDto<Void> - Status
	 */
	@Override
	public ResponseDto<Void> forgotPassword(String email) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.FAILURE;
		
		if (StringUtils.validateEmail(email)) {
			User user = userService.getCustomUserByMail(email);
			if (user != null) {
				// generates new password of length 10
				String newPassword = StringUtils.randomAlphanumeric(10);
				try {
					status = userService.changePassword(user, MD5Encryption.encrypt(newPassword));
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
		
				if (status != Status.FAILURE) {
					String emailBody = "Hello "
							+ user.getName()
							+ ",\nYour new password is: \n"
							+ newPassword
							+ "\n\nYou can use this password for login and you can change the password from the user profile.\n\nThanks & Regards,\nWeSurve Helpline";
					try {
						EmailUtils.sendEmail("wesurvehelpline@gmail.com",
								"wesurve#123", email, "Recover Password",
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

	/**
	 * This method returns list of all users except user with given user id.
	 * @param userId
	 * @return Iterable<UserDetailsDto>
	 */
	@Override
	public Iterable<UserDetailsDto> getAllUsers(int userId) {
		Set<UserDetailsDto> userDetailsDtoSet = new HashSet<>();
		Iterable<User> iterableOfUsers = userService.getAllUsers();
		if (iterableOfUsers != null) {
			for (User user : iterableOfUsers) {
				//skip user with given user id.
				if (user.getUserId() == userId) {
					continue;
				}

				userDetailsDtoSet.add(modelToDto(user));
			}
		}

		return userDetailsDtoSet;
	}

	/**
	 * This method converts User Model object to UserDetailsDto object
	 * @param user - User object
	 * @return UserDetailsDto object
	 */
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

	/**
	 * This method logs out the current user.
	 * @param userId
	 * @return Status
	 */
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

	/**
	 * This method checks authorisation of the user.
	 * @param accessToken
	 * @return UserData object
	 */
	@Override
	public UserData checkAuthorization(String accessToken) {
		return userService.checkAuthorization(accessToken);
	}

	/**
	 * @param userId id of the user whose role needs to changed
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
				
				// Sets all surveys Dead when user role changes from Surveyor to normal user.
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

	/**
	 * This method returns list of all surveys of which a user can view result.
	 * @param userId
	 * @return Iterable<SurveyInfoDto>
	 */
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

	/**
	 * This method converts Survey Model object to SurveyInfoDto object.
	 * @param curSurvey - Survey object
	 * @return SurveyInfoDto object
	 */
	private SurveyInfoDto convertModeltoDtoSurveyInfo(Survey survey) {
		SurveyInfoDto surveyInfoDtoObject = new SurveyInfoDto();
		
		surveyInfoDtoObject.setId(survey.getSurveyId());
		surveyInfoDtoObject.setDescription(survey.getDescription());
		surveyInfoDtoObject.setSurveyName(survey.getSurveyName());
		surveyInfoDtoObject.setStatus(survey.getSurveyStatus());
		surveyInfoDtoObject.setSurveyUrl(surveyService.getSurveyURL(survey));
		
		String labels = convertModelToDtoLabel(survey.getLabels());
		if (labels.length() > 1) {
			labels = labels.substring(0, labels.length() - 1);
		}

		surveyInfoDtoObject.setLabels(labels);
		return surveyInfoDtoObject;
	}

	/**
	 * This method converts set of Labels object to String separated by comma.
	 * @param labels - set of Labels.
	 * @return String - label string
	 */
	private String convertModelToDtoLabel(Set<Labels> labels) {
		String label = "";
		if (labels != null) {
			for (Labels currObj : labels) {
				label += currObj.getLabelName() + ",";
			}
		}

		return label;
	}

	/**
	 * This method changes the password.
	 * @param userId - user id whose password has to be changed
	 * @param currentPassword 
	 * @param newPassword
	 * @return
	 */
	@Override
	public ResponseDto<Void> changePassword(int userId, String currentPassword, String newPassword) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.FAILURE;
		User user = userService.getUserById(userId);

		try {
			boolean condition = StringUtils.validateString(newPassword)
					&& StringUtils.validateString(currentPassword)
					&& newPassword.length() >= 8;
			if (condition) {
				if (currentPassword.equals(newPassword)) {
					status = Status.DUPLICATE;
				} else if (user != null && user.getPassword().equals(MD5Encryption.encrypt(currentPassword))) {
					user.setPassword(MD5Encryption.encrypt(newPassword));
					status = userService.update(user);
				} else {
					status = Status.FAILURE;
				}
			} else {
				status = Status.INVALID_CONTENT;
			}
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}

		response.setStatus(status);
		return response;
	}

	/**
	 * This method returns list of all surveys created by the user with user id.
	 * @param userId 
	 * @return Iterable<SurveyInfoDto>
	 */
	@Override
	public Iterable<SurveyInfoDto> getSurveyListOfSurveyor(int userId) {
		User user = userService.getUserById(userId);
		Set<SurveyInfoDto> surveyInfoList = new HashSet<>();
	
		Set<Survey> surveys = user.getCreatedSurveyList();
		for (Survey curSurvey : surveys) {
			if (curSurvey.getSurveyStatus() != SurveyStatus.DELETED) {
				surveyInfoList.add(convertModeltoDtoSurveyInfo(curSurvey));
			}
		}

		return surveyInfoList;
	}

	/**
	 * This method returns list of all surveys filled by the user with user id.
	 * @param userId
	 * @return Iterable<SurveyInfoDto>
	 */
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

	/**
	 * This method returns list of all users for surveyor according to the particular survey id.
	 * @param surveyorId
	 * @param surveyId
	 * @return Iterable<UserDetailsForSurveyorDto>
	 */
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

	/**
	 * This method converts User model object to UserDetailsForSurveyorDto object. 
	 * @param user
	 * @return UserDetailsForSurveyorDto object
	 */
	private UserDetailsForSurveyorDto modelToDtoForSurveyor(User user) {
		UserDetailsForSurveyorDto curUser = new UserDetailsForSurveyorDto();
		curUser.setId(user.getUserId());
		curUser.setEmail(user.getEmail());
		curUser.setName(user.getName());
		return curUser;
	}
}
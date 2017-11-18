package com.metacube.wesurve.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.PasswordsDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDetailsForSurveyorDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;
import com.metacube.wesurve.utils.Constants;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserFacade userFacade;

	/**
	 * @param userDto contains the details user that register contains email, password, dob and name
	 * @return the status, message and access token for the user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<Void> createNewUser(@RequestBody UserDto userDto) {
		return userFacade.createNewUser(userDto);
	}

	/**
	 * @param request
	 * @param loginDto contains the login credential of the user
	 * @return the success status if the user is valid else 400 status will be returned
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDto<LoginResponseDto> login(HttpServletRequest request,
			@RequestBody LoginCredentialsDto loginDto) {
		return userFacade.login(loginDto);
	}

	/**
	 * @param request
	 * @param socialLoginCredentials contains user details, email, dob, user name
	 * @return status, message, accessToken, role, viewer, name, email of the user
	 */
	@RequestMapping(value = "/sociallogin", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDto<LoginResponseDto> socialLogin(@RequestBody UserDto socialLoginCredentials) {
		return userFacade.socialLogin(socialLoginCredentials);
	}

	/**
	 * @param request
	 * @param baseDto receiving token of the logged in user
	 * @return Status of the user logged out or not function to logout the user from the session
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDto<Void> logout(@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		Status status = Status.FAILURE;
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() != Role.INVALID) {
			status = userFacade.logout(currentUser.getUserId());
		}

		ResponseDto<Void> response = new ResponseDto<>();
		response.setStatus(status);
		return response;
	}

	/**
	 * @param email of the user whose password need to reset
	 * @return the status
	 */
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Void> forgotPassword(@RequestParam(Constants.EMAIL) String email) {
		return userFacade.forgotPassword(email);
	}

	/**
	 * @param accessToken token of the requester
	 * @return list of the user
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseDto<Iterable<UserDetailsDto>> getAllUser(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		UserData currentUser = checkAuthorization(accessToken);
		Role role = currentUser.getRole();
		Iterable<UserDetailsDto> userList = null;
		Status status = Status.ACCESS_DENIED;

		if (role == Role.ADMIN) {
			status = Status.ACCESS_GRANTED;
			userList = userFacade.getAllUsers(currentUser.getUserId());
		}

		ResponseDto<Iterable<UserDetailsDto>> response = new ResponseDto<>();
		response.setStatus(status);
		response.setBody(userList);
		return response;
	}
	/**
	 * This method returns users list to the surveyor
	 * @param accessToken
	 * @param surveyId
	 * @return ResponseDto<Iterable<UserDetailsForSurveyorDto>>
	 */
	@RequestMapping(value = "/surveyor/userlist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseDto<Iterable<UserDetailsForSurveyorDto>> getUserToAssignViewer(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {
		UserData currentUser = checkAuthorization(accessToken);
		Role role = currentUser.getRole();
		Iterable<UserDetailsForSurveyorDto> userList = null;
		Status status = Status.ACCESS_DENIED;

		if (role == Role.SURVEYOR) {
			status = Status.ACCESS_GRANTED;
			userList = userFacade.getAllUsersForSurveyor(currentUser.getUserId(), surveyId);
		}

		ResponseDto<Iterable<UserDetailsForSurveyorDto>> response = new ResponseDto<>();
		response.setStatus(status);
		response.setBody(userList);
		return response;
	}

	/**
	 * This method changes password of the user
	 * @param accessToken
	 * @param password
	 * @return ResponseDto<Void> object
	 */
	@RequestMapping(value = "/changepassword", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> changePassword(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestBody PasswordsDto password) {
		ResponseDto<Void> response = null;

		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() != Role.INVALID) {
			response = userFacade.changePassword(currentUser.getUserId(), password.getCurrentPassword(), password.getNewPassword());
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	/**
	 * @param token access token of the user
	 * @param userId is the id of the user who's role needs to be changed
	 * @return the Status function to create or remove surveyor
	 */
	@RequestMapping(value = "/createsurveyor/{userId}", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<Void> changeRoleofUser(@RequestHeader(value = Constants.ACCESSTOKEN) String token,
			@PathVariable int userId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.FAILURE;
		if (Role.ADMIN == checkAuthorization(token).getRole()) {
			status = userFacade.changeUserRole(userId);
		}

		response.setStatus(status);
		return response;
	}

	/**
	 * This method returns survey list for the viewer to see result of the survey.
	 * @param accessToken
	 * @return ResponseDto<Iterable<SurveyInfoDto>> object
	 */ 
	@RequestMapping(value = "/viewer/surveylist", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Iterable<SurveyInfoDto>> getSurveyListForViewers(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		UserData currentUser = checkAuthorization(accessToken);
		
		ResponseDto<Iterable<SurveyInfoDto>> response = new ResponseDto<>();
		Iterable<SurveyInfoDto> surveyList = null;
		Status status = Status.ACCESS_DENIED;
		if (Role.INVALID != currentUser.getRole()) {
			surveyList = userFacade.getSurveyListForViewers(currentUser.getUserId());
			status = Status.SUCCESS;
		}

		response.setStatus(status);
		response.setBody(surveyList);
		return response;
	}

	/**
	 * This method returns created survey's list of the surveyor.
	 * @param accessToken
	 * @return ResponseDto<Iterable<SurveyInfoDto>>
	 */
	@RequestMapping(value = "/surveyor/surveylist", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Iterable<SurveyInfoDto>> getSurveyListOfSurveyor(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		UserData currentUser = checkAuthorization(accessToken);
		ResponseDto<Iterable<SurveyInfoDto>> response = new ResponseDto<>();
		Iterable<SurveyInfoDto> surveyList = null;
		Status status = Status.ACCESS_DENIED;

		if (Role.SURVEYOR == currentUser.getRole()) {
			surveyList = userFacade.getSurveyListOfSurveyor(currentUser.getUserId());
			status = Status.SUCCESS;
		}

		response.setStatus(status);
		response.setBody(surveyList);
		return response;
	}

	/**
	 * This method returns list of filled surveys of the usr to view responses.
	 * @param accessToken
	 * @return ResponseDto<Iterable<SurveyInfoDto>>
	 */
	@RequestMapping(value = "/responder/surveylist", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Iterable<SurveyInfoDto>> getListOfFilledSurveys(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		ResponseDto<Iterable<SurveyInfoDto>> response = new ResponseDto<>();
		UserData currentUser = checkAuthorization(accessToken);
		Iterable<SurveyInfoDto> surveyList = null;
		Status status = Status.ACCESS_DENIED;

		if (Role.INVALID != currentUser.getRole()) {
			surveyList = userFacade.getListOfFilledSurveys(currentUser.getUserId());
			status = Status.SUCCESS;
		}

		response.setStatus(status);
		response.setBody(surveyList);
		return response;
	}

	/**
	 * This method checks authorisation of the user using access token
	 * @param accessToken of the user
	 * @return UserData object
	 */
	private UserData checkAuthorization(String accessToken) {
		UserData user = new UserData();
		Role role = Role.INVALID;
		if (accessToken != null) {
			user = userFacade.checkAuthorization(accessToken);
		} else {
			user.setRole(role);
		}
	
		return user;
	}
}
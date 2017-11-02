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

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.dto.UserListResponse;
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
	 * @param userDto
	 *            contains the details user that register contains email ,password,
	 *            dob and name
	 * @return the status, message and access token for the user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDto userDto) {
		return userFacade.createNewUser(userDto);
	}

	/**
	 * @param request
	 * @param loginDto
	 *            contains the login credential of the user
	 * @return the success status if the user is valid else 400 status will be
	 *         returned
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto login(HttpServletRequest request, @RequestBody LoginCredentialsDto loginDto) {
		return userFacade.login(loginDto);
	}

	/**
	 * @param request
	 * @param socialLoginCredentials
	 *            contains user details, email, dob, user name
	 * @return status, message, accessToken, role, viewer, name, email of the user
	 */
	@RequestMapping(value = "/sociallogin", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto socialLogin(HttpServletRequest request,
			@RequestBody UserDto socialLoginCredentials) {
		return userFacade.socialLogin(socialLoginCredentials);
	}

	/**
	 * @param request
	 * @param baseDto
	 *            receiving token of the logged in user
	 * @return Status of the user logged out or not function to logout the user from
	 *         the session
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Status logout(@RequestHeader(value = "X-auth") String accessToken) {
		Status status = Status.FAILURE;
		if (checkAuthorization(accessToken) != Role.INVALID) {
			status = userFacade.logout(accessToken);
		}

		return status;
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public @ResponseBody Status forgotPassword(@RequestParam("email") String email) {
		return userFacade.forgotPassword(email);
	}

	@RequestMapping(value = "/userslist", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody UserListResponse getAllUser(@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken) {
		Role role = checkAuthorization(accessToken);
		UserListResponse userListResponse = new UserListResponse();
		if (role == Role.ADMIN) {
			userListResponse.setAccess(true);
			userListResponse.setUsersList(userFacade.getAllUsers(accessToken));
		} else {
			userListResponse.setAccess(false);
			userListResponse.setUsersList(null);
		}

		return userListResponse;
	}

	@RequestMapping(value = "/createsurveyor/{email}", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status changeRoleofUser(@RequestHeader(value = Constants.ACCESSTOKEN) String token, @PathVariable String email) {
		Status status = Status.FAILURE;
		if(Role.ADMIN == checkAuthorization(token)) {
			status = userFacade.changeUserRole(token, email);
		}
		
		return status;
	}

	@RequestMapping(value = "/welcome")
	public @ResponseBody String welcome() {
		return "Hello";
	}

	private Role checkAuthorization(String accessToken) {
		Role role = Role.INVALID;
		if (accessToken != null) {
			role = userFacade.checkAuthorization(accessToken);
		}

		return role;
	}
}
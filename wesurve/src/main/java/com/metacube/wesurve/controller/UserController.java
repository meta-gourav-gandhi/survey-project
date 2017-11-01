package com.metacube.wesurve.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.BaseDto;
import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;

@Controller
@CrossOrigin
@RequestMapping("/user")
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserController {

	HttpSession session;
	
	@Autowired
	UserFacade userFacade;

	/**
	 * @param userDto contains the details user that register contains email ,password dob and name
	 * @return the status, message and access token for the user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDto userDto) {
		return userFacade.createNewUser(userDto);
	}

	/**
	 * @param request
	 * @param loginDto contains the login credential of the user
	 * @return the success status if the user is valid else 404 status will be returned
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto login(HttpServletRequest request, @RequestBody LoginCredentialsDto loginDto) {
		session = request.getSession();
		System.out.println(session);
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if (session.getAttribute("CurrentUserDetails") == null) {
			loginResponseDto = userFacade.login(loginDto);
			session.setAttribute("CurrentUserDetails", loginResponseDto);
		} else {
			loginResponseDto = (LoginResponseDto) session.getAttribute("CurrentUserDetails");
			System.out.println(loginResponseDto.getEmail());
		}

		return loginResponseDto;
	}

	/**
	 * @param request
	 * @param socialLoginCredentials contains user details, email, dob, user name
	 * @return status, message, accessToken, role, viewer, name, email of the user
	 */
	@RequestMapping(value = "/sociallogin", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto socialLogin(HttpServletRequest request, @RequestBody UserDto socialLoginCredentials) {
		session = request.getSession();

		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if (session.getAttribute("CurrentUserDetails") == null) {
			loginResponseDto = userFacade.socialLogin(socialLoginCredentials);
			session.setAttribute("CurrentUserDetails", loginResponseDto);
		} else {
			loginResponseDto = (LoginResponseDto) session.getAttribute("CurrentUserDetails");
			loginResponseDto.setMessage("Already logged in");
			System.out.println(loginResponseDto.getEmail());
		}

		return loginResponseDto;
	}

	/**
	 * @param request
	 * @param baseDto receiving token of the logged in user 
	 * @return Status of the user logged out or not function to logout the user from the session
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Status logout(HttpServletRequest request, @RequestBody BaseDto baseDto) {
		session = request.getSession(false);
		Status status = Status.FAILURE;
		System.out.println(baseDto.getAccessToken());
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		System.out.println(session);
		loginResponseDto = (LoginResponseDto) session.getAttribute("CurrentUserDetails");
		if(loginResponseDto != null) {
			System.out.println("session is not null");
			System.out.println(loginResponseDto.getEmail());
		} else {
			System.out.println("session is null");
		}
		
		if (loginResponseDto != null && baseDto.getAccessToken().equals(loginResponseDto.getAccessToken())) {
			System.out.println("Token Matched");
			status = Status.SUCCESS;
			session.invalidate();
		}

		return status;
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public @ResponseBody Status forgotPassword(@PathVariable String email) {
		return userFacade.forgotPassword(email);
	}

	@RequestMapping(value = "/welcome")
	public @ResponseBody String welcome() {
		return "Hello";
	}
}
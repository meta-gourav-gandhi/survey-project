package com.metacube.wesurve.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.metacube.wesurve.model.User;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	HttpSession session;
	
	@Autowired
	UserFacade userFacade;

	/**
	 * @param userDto contains the details user that register contais email ,password 
	 * dob and name
	 * @return the status , message  and access token for the user
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
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if(session.getAttribute("CurrentUserDetails") == null) {
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
	 * @param socialLoginCredentials contains user details ,email,dob, user name 
	 * @return status,message,accessToken,role,viewer,name,email of the user
	 */
	@RequestMapping(value = "/sociallogin", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto socialLogin(HttpServletRequest request, @RequestBody UserDto socialLoginCredentials) {
		session = request.getSession();
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		if(session.getAttribute("CurrentUserDetails") == null) {
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
	 * @return Status of the user logged out or not
	 * function to logout the user from the session
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Status logout(HttpServletRequest request, @RequestBody  BaseDto baseDto) {
		session = request.getSession();
		Status status = Status.FAILURE;
		User curruser = (User) session.getAttribute("CurrentUserDetails");
		
		if(baseDto.getAccessToken().equals(curruser.getToken())) {
			status = Status.SUCCESS;
			session.invalidate();
		}
		
		return status;
	}

	@RequestMapping(value = "/welcome")
	public @ResponseBody String a() {
		System.out.println(session.getAttribute("A"));
		//userFacade.sendEmail("wesurvehelpline@gmail.com", "wesurve#123", "priya.mishra.28496@gmail.com", "subject", "EmailBody");
		return "Hello";
	}
}
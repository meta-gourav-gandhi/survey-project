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

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	HttpSession session;
	
	@Autowired
	UserFacade userFacade;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDto userDto) {
		return userFacade.createNewUser(userDto);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody LoginResponseDto login(HttpServletRequest request, @RequestBody LoginCredentialsDto loginDto) {
		session = request.getSession();
		LoginResponseDto loginResponseDto = userFacade.login(loginDto);
		session.setAttribute("CurrentUserDetails", loginResponseDto);
		return loginResponseDto;
	}

	@RequestMapping(value = "/welcome")
	public @ResponseBody String a() {
		System.out.println(session.getAttribute("A"));
		//userFacade.sendEmail("wesurvehelpline@gmail.com", "wesurve#123", "priya.mishra.28496@gmail.com", "subject", "EmailBody");
		return "Hello";
	}
}
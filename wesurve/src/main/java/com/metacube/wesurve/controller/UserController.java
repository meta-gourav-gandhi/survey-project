package com.metacube.wesurve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserDetailsFacade;

@Controller
@RequestMapping("/user")

public class UserController
{
	@Autowired
	UserDetailsFacade userDetailsFacade;
	
	@RequestMapping(value = "/register", produces = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDetailsDto userDetailsDto) {
		return userDetailsFacade.createNewUser(userDetailsDto);
	}
	
	@RequestMapping(value = "/a", produces = "application/json")
	public @ResponseBody String a() {
		return "Hello";
	}
}
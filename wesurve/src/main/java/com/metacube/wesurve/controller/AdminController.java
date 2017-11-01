package com.metacube.wesurve.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;

public class AdminController {
	UserFacade userFacade;
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDto userDto) {
		return userFacade.createNewUser(userDto);
	}
}
package com.metacube.wesurve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserFacade userFacade;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Status createNewUser(@RequestBody UserDto userDto) {
		System.out.println("controller"+ userDto.getEmail());
		return userFacade.createNewUser(userDto);
	}

	@RequestMapping(value = "/a")
	public @ResponseBody String a() {
		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		System.out.println("here:  " + name);
		*/
		userFacade.sendEmail("wesurvehelpline@gmail.com", "wesurve#123", "priya.mishra.28496@gmail.com", "subject", "EmailBody");
		return "Hello";
	}
}
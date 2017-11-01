package com.metacube.wesurve.controller;

import javax.servlet.http.HttpServletRequest;
//"X-auth"
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.BaseDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.enums.Roles;
import com.metacube.wesurve.facade.UserFacade;
import com.metacube.wesurve.utils.UserUtils;

@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UserFacade userFacade;

	@RequestMapping(value = "/getallusers", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Iterable<UserDetailsDto> getAllUser(HttpServletRequest request, @RequestBody BaseDto baseDto) {
		HttpSession session;
		session = request.getSession();
		LoginResponseDto loginResponseDto = (LoginResponseDto) session.getAttribute("CurrentUserDetails");
		String token = request.getHeader("X-auth");
		token = baseDto.getAccessToken();
		Roles role = UserUtils.checkAuthorization(token, loginResponseDto);
		Iterable<UserDetailsDto> userDetailsDtolist;
		if (role == Roles.ADMIN) {
			userDetailsDtolist = userFacade.getAllUsers();
		} else {
			userDetailsDtolist = null;
		}

		return userDetailsDtolist;
	}
}

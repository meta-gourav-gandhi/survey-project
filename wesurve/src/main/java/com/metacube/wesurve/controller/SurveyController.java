package com.metacube.wesurve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.SurveyFacade;
import com.metacube.wesurve.utils.Constants;

@Controller
@CrossOrigin
@RequestMapping("/survey")
public class SurveyController {
	@Autowired
	SurveyFacade surveyFacade;
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<SurveyResponseDto> createSurvey(@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam("id") int surveyorId, @RequestBody SurveyDto surveyDto) {
		
		ResponseDto<SurveyResponseDto> response;
		if(checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.createSurvey(surveyorId, surveyDto);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}
		
		return response;
	}
	
	public Role checkAuthorization(String accessToken) {
		Role role = Role.INVALID;
		if (accessToken != null) {
			role = surveyFacade.checkAuthorization(accessToken);
		}

		return role;
	}
}
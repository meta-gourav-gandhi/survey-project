/**
 * The class SurveyController is a controller level class for all operations related surveys.
 */
package com.metacube.wesurve.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.dto.SurveyResultDto;
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

	/**
	 * This method
	 * 
	 * @param accessToken
	 * @param surveyDto
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<SurveyResponseDto> createSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestBody SurveyDto surveyDto) {
		
		ResponseDto<SurveyResponseDto> response;
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() == Role.SURVEYOR) {
			response = surveyFacade.createSurvey(currentUser.getUserId(), surveyDto);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public @ResponseBody ResponseDto<Void> deleteSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {

		ResponseDto<Void> response;
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() == Role.SURVEYOR) {
			response = surveyFacade.deleteSurvey(currentUser.getUserId(), surveyId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Void> checkIfSurveyExists(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;

		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() != Role.INVALID) {
			status = surveyFacade.checkIfSurveyExists(surveyId, currentUser.getUserId());
		}

		response.setStatus(status);
		return response;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<SurveyDto> getSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {
		
		ResponseDto<SurveyDto> response;
		
		if (checkAuthorization(accessToken).getRole() != Role.INVALID) {
			response = surveyFacade.getSurvey(surveyId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> editSurvey(@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestBody SurveyDto surveyDto) {
		ResponseDto<Void> response;
		
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() == Role.SURVEYOR) {
			response = surveyFacade.editSurvey(currentUser.getUserId(), surveyDto);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	@RequestMapping(value = "/changestatus", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> changeSurveyStatus(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {
		ResponseDto<Void> response;
		
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() == Role.SURVEYOR) {
			response = surveyFacade.changeSurveyStatus(currentUser.getUserId(), surveyId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	@RequestMapping(value = "/edit/viewer", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> addOrRemoveSurveyViewer(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId, @RequestParam(Constants.USERID) int userId) {
		ResponseDto<Void> response;
		
		UserData currentUser = checkAuthorization(accessToken);
		if (currentUser.getRole() == Role.SURVEYOR) {
			response = surveyFacade.addOrRemoveSurveyViewer(currentUser.getUserId(), surveyId, userId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	@RequestMapping(value = "/save/response", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<Void> saveSurveyResponse(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestBody ResponderDto ResponderDto) {

		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;
		
		UserData currentUser = checkAuthorization(accessToken);
		if (Role.INVALID != currentUser.getRole()) {
			status = surveyFacade.saveResponse(currentUser.getUserId(), ResponderDto);
		}

		response.setStatus(status);
		return response;
	}

	@RequestMapping(value = "/response", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Map<Integer, String>> getSurveyResponse(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {
		ResponseDto<Map<Integer, String>> response = new ResponseDto<>();

		UserData currentUser = checkAuthorization(accessToken);
		if (Role.INVALID != currentUser.getRole()) {
			response = surveyFacade.getSuveyResponse(currentUser.getUserId(), surveyId);
		} else {
			Status status = Status.ACCESS_DENIED;
			response.setStatus(status);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<SurveyResultDto> getSurveyResult(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam(Constants.SURVEYID) int surveyId) {

		ResponseDto<SurveyResultDto> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;

		UserData currentUser = checkAuthorization(accessToken);
		if (Role.INVALID != currentUser.getRole()) {
			response = surveyFacade.getSurveyResult(currentUser.getUserId(), surveyId);

		} else {
			status = Status.ACCESS_DENIED;
			response.setStatus(status);
		}

		return response;
	}

	/**
	 * @param accessToken of the user
	 * @return the role of the given user
	 */
	private UserData checkAuthorization(String accessToken) {
		UserData user = new UserData();
		Role role = Role.INVALID;
		if (accessToken != null) {
			user = surveyFacade.checkAuthorization(accessToken);
		} else {
			user.setRole(role);
		}
	
		return user;
	}
}
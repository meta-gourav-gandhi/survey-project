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

import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
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

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<SurveyResponseDto> createSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, 
			@RequestBody SurveyDto surveyDto) {

		ResponseDto<SurveyResponseDto> response;
		if (checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.createSurvey(accessToken, surveyDto);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public @ResponseBody ResponseDto<Void> deleteSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {

		ResponseDto<Void> response;
		if (checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.deleteSurvey(accessToken, surveyId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Void> checkIfSurveyExists(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;
		;
		if (checkAuthorization(accessToken) != Role.INVALID) {
			status = surveyFacade.checkIfSurveyExists(surveyId);
		}

		response.setStatus(status);
		return response;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<SurveyDto> getSurvey(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {
		ResponseDto<SurveyDto> response;
		if (checkAuthorization(accessToken) != Role.INVALID) {
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
		if (checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.editSurvey(accessToken, surveyDto);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	@RequestMapping(value = "/changestatus", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> changeSurveyStatus(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {
		ResponseDto<Void> response;
		if (checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.changeSurveyStatus(accessToken, surveyId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	@RequestMapping(value = "/edit/viewer", method = RequestMethod.PUT)
	public @ResponseBody ResponseDto<Void> addOrRemoveSurveyViewer(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId,
			@RequestParam("userid") int userId) {
		ResponseDto<Void> response;
		if (checkAuthorization(accessToken) == Role.SURVEYOR) {
			response = surveyFacade.addOrRemoveSurveyViewer(accessToken, surveyId, userId);
		} else {
			response = new ResponseDto<>();
			response.setStatus(Status.ACCESS_DENIED);
		}

		return response;
	}

	/**
	 * method to search the survey for the surveyor and returning the matched
	 * surveys either by name or label in the string
	 *
	 * @param accessToken
	 * @return
	 */
	@RequestMapping(value = "/searchsurvey", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Iterable<SurveyInfoDto>> searchSurveyForSurveyor(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken,
			@RequestParam("query") String searchString) {

		ResponseDto<Iterable<SurveyInfoDto>> response = new ResponseDto<>();
		Iterable<SurveyInfoDto> surveyList = null;
		Status status = Status.ACCESS_DENIED;
		if (Role.SURVEYOR == checkAuthorization(accessToken)) {
			surveyList = surveyFacade.searchSurvey(accessToken, searchString);
			status = Status.SUCCESS;

		}

		response.setStatus(status);
		response.setBody(surveyList);
		return response;
	}

	@RequestMapping(value = "/save/response", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseDto<Void> saveSurveyResponse(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestBody ResponderDto ResponderDto) {

		ResponseDto<Void> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;
		if (Role.INVALID != checkAuthorization(accessToken)) {
			status = surveyFacade.saveResponse(accessToken, ResponderDto);
		}

		response.setStatus(status);
		return response;
	}

	@RequestMapping(value = "/response", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<Map<Integer, String>> getSurveyResponse(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {
		ResponseDto<Map<Integer, String>> response = new ResponseDto<>();

		if (Role.INVALID != checkAuthorization(accessToken)) {
			response = surveyFacade.getSuveyResponse(accessToken, surveyId);
		} else {
			Status status = Status.ACCESS_DENIED;
			response.setStatus(status);
			response.setBody(null);
		}

		return response;
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public @ResponseBody ResponseDto<SurveyResultDto> getSurveyResult(
			@RequestHeader(value = Constants.ACCESSTOKEN) String accessToken, @RequestParam("id") int surveyId) {

		ResponseDto<SurveyResultDto> response = new ResponseDto<>();
		Status status = Status.ACCESS_DENIED;

		if (Role.INVALID != checkAuthorization(accessToken)) {
			response = surveyFacade.getSurveyResult(accessToken, surveyId);

		} else {
			status = Status.ACCESS_DENIED;
			response.setStatus(status);
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
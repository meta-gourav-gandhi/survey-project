package com.metacube.wesurve.facade;

import java.util.Map;

import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.dto.SurveyResultDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;

public interface SurveyFacade {
	ResponseDto<SurveyResponseDto> createSurvey(String accessToken, SurveyDto surveyDto);
	Role checkAuthorization(String accessToken);
	ResponseDto<Void> deleteSurvey(String accessToken, int surveyId);
	ResponseDto<SurveyDto> getSurvey(int surveyId);
	ResponseDto<Void> editSurvey(String accessToken, SurveyDto surveyDto);
	ResponseDto<Void> changeSurveyStatus(String accessToken, int surveyId);
	ResponseDto<Void> addOrRemoveSurveyViewer(String accessToken, int surveyId, int userId);
	Status checkIfSurveyExists(int surveyId, String accessToken);
	Status saveResponse(String accessToken, ResponderDto responderDto);
	ResponseDto<SurveyResultDto> getSurveyResult(String accessToken, int surveyId);
	ResponseDto<Map<Integer, String>> getSuveyResponse(String accessToken, int surveyId);
}
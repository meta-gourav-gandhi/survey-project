/**
 * The SurveyFacade is an interface containing all methods related to survey.
 */
package com.metacube.wesurve.facade;

import java.util.Map;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.dto.SurveyResultDto;
import com.metacube.wesurve.enums.Status;

public interface SurveyFacade {
	ResponseDto<SurveyResponseDto> createSurvey(int surveyorId, SurveyDto surveyDto);
	UserData checkAuthorization(String accessToken);
	ResponseDto<Void> deleteSurvey(int surveyorId, int surveyId);
	ResponseDto<SurveyDto> getSurvey(int surveyId);
	ResponseDto<Void> editSurvey(int surveyorId, SurveyDto surveyDto);
	ResponseDto<Void> changeSurveyStatus(int surveyorId, int surveyId);
	ResponseDto<Void> addOrRemoveSurveyViewer(int surveyorId, int surveyId, int userId);
	Status getSurveyStatus(int surveyId, int userId);
	Status saveResponse(int responderId, ResponderDto responderDto);
	ResponseDto<SurveyResultDto> getSurveyResult(int viewerId, int surveyId);
	ResponseDto<Map<Integer, String>> getSuveyResponse(int responderId, int surveyId);
}
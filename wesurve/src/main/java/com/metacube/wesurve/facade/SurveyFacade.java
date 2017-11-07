package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.enums.Role;

public interface SurveyFacade {
	ResponseDto<SurveyResponseDto> createSurvey(int surveyorId, SurveyDto surveyDto);
	Role checkAuthorization(String accessToken);
	ResponseDto<Void> deleteSurvey(String accessToken, int surveyId);
}
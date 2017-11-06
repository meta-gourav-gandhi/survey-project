package com.metacube.wesurve.service;

import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Survey;

public interface SurveyService {
	Survey createSurvey(Survey survey);
	void changeSurveyStatus(Survey survey, SurveyStatus status);
}
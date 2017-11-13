package com.metacube.wesurve.service;

import java.util.Set;

import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;

public interface SurveyService {
	Survey createSurvey(Survey survey);
	Status changeSurveyStatus(Survey survey, SurveyStatus status);
	Survey getSurveyById(int surveyId);
	Status deleteSurvey(Survey survey);
	String getSurveyURL(Survey survey);
	Status edit(Survey survey);
	Status addViewer(Survey survey, User viewer);
	Status removeViewer(Survey survey, User viewer);
	Set<Survey> getMatchedSurveys(User surveyor, String searchString);
	
}
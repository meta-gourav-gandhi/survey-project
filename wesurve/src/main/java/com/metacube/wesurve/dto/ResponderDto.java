package com.metacube.wesurve.dto;

import java.util.List;

public class ResponderDto {
	private int surveyId;
	private List<QuestionResponseDto> questionResponses;
	
	public int getSurveyId() {
		return surveyId;
	}
	
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	
	public List<QuestionResponseDto> getQuestionResponses() {
		return questionResponses;
	}
	
	public void setQuestionResponses(List<QuestionResponseDto> questionResponses) {
		this.questionResponses = questionResponses;
	}
}
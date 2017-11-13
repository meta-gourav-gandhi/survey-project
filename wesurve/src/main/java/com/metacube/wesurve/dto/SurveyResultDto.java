package com.metacube.wesurve.dto;

import java.util.Set;

public class SurveyResultDto {
	private int surveyId;
	private int numOfResponders;
	private Set<QuestionResultDto> questionResults;
	
	public int getSurveyId() {
		return surveyId;
	}
	
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	
	public int getNumOfResponders() {
		return numOfResponders;
	}
	
	public void setNumOfResponders(int numOfResponders) {
		this.numOfResponders = numOfResponders;
	}

	public Set<QuestionResultDto> getQuestionResults() {
		return questionResults;
	}

	public void setQuestionResults(Set<QuestionResultDto> questionResults) {
		this.questionResults = questionResults;
	}
}
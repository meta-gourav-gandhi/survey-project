package com.metacube.wesurve.model;

import java.io.Serializable;

public class SurveyResponsesID implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private int surveyId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
}
package com.metacube.wesurve.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(SurveyResponsesID.class)
@Table(name = "survey_responses")
public class SurveyResponses {

	@Id
	@Column(name = "user_id")
	private int userId;

	@Id
	@Column(name = "survey_id")
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
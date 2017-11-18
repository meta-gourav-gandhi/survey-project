/**
 * DTO class
 */
package com.metacube.wesurve.dto;

import com.metacube.wesurve.enums.SurveyStatus;

public class SurveyInfoDto {

	private int id;
	private String surveyName;
	private String description;
	private SurveyStatus status;
	private String surveyUrl;
	private String labels;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SurveyStatus getStatus() {
		return status;
	}

	public void setStatus(SurveyStatus status) {
		this.status = status;
	}

	public String getSurveyUrl() {
		return surveyUrl;
	}

	public void setSurveyUrl(String surveyUrl) {
		this.surveyUrl = surveyUrl;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}
}
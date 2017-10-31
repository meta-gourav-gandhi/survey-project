package com.metacube.wesurve.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.metacube.wesurve.enums.SurveyStatus;

@Entity
@Table(name="survey")
public class Survey {

	@Id
	@Column(name = "survey_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int surveyId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;
	
	@Column(name = "survey_name", length = 500, nullable = false)
	private String surveyName;
	
	@Enumerated(EnumType.STRING)
	private SurveyStatus surveyStatus;
	
	@Column(name = "created_date", nullable = true)
	private Date createdDate;
	
	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public SurveyStatus getSurveyStatus() {
		return surveyStatus;
	}

	public void setSurveyStatus(SurveyStatus surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
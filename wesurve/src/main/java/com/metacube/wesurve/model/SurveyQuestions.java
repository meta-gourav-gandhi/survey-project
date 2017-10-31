package com.metacube.wesurve.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="survey_questions")
public class SurveyQuestions {

	@Id
	@Column(name = "ques_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int quesId;
	
	@ManyToOne
	@JoinColumn(name = "survey_id")
	private Survey surveyId;
	
	
	@Column(name = "question", length = 500, nullable = false)
	private String question;
	
	@Column(name = "created_date", nullable = true)
	private Date createdDate;
	
	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public Survey getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Survey surveyId) {
		this.surveyId = surveyId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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

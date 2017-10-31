package com.metacube.wesurve.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="survey_labels")
public class SurveyLabels {

	@Id
	@Column(name = "label_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labelId;
	
	@ManyToOne
	@JoinColumn(name = "survey_id")
	private Survey surveyId;
	
	@Column(name = "survey_label", length = 50, nullable = false)
	private String surveyLabel;

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public Survey getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Survey surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyLabel() {
		return surveyLabel;
	}

	public void setSurveyLabel(String surveyLabel) {
		this.surveyLabel = surveyLabel;
	}
	
	
	
	
}

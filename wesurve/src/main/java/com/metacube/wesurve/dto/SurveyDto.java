/**
 * DTO class
 */
package com.metacube.wesurve.dto;

import java.util.Set;

import com.metacube.wesurve.enums.SurveyStatus;

public class SurveyDto {
	private int id;
	private String name;
	private String description;
	private Set<QuestionDto> questions;
	private Set<String> labels;
	private SurveyStatus status;
	private boolean viewer;
	private boolean owner;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<QuestionDto> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<QuestionDto> questions) {
		this.questions = questions;
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	public SurveyStatus getStatus() {
		return status;
	}

	public void setStatus(SurveyStatus status) {
		this.status = status;
	}

	public boolean isViewer() {
		return viewer;
	}

	public void setViewer(boolean viewer) {
		this.viewer = viewer;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}
}
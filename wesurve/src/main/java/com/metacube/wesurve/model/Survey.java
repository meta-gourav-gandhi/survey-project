package com.metacube.wesurve.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.metacube.wesurve.enums.SurveyStatus;

@Entity
@Table(name = "survey")
public class Survey {

	@Id
	@Column(name = "survey_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int surveyId;
	
	@Column(name = "survey_name", length = 500, nullable = false)
	private String surveyName;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User surveyOwner;

	@Enumerated(EnumType.STRING)
	private SurveyStatus surveyStatus = SurveyStatus.NOTLIVE;
	
	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "created_date", nullable = true)
	private Date createdDate;

	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "survey_label", joinColumns = { @JoinColumn(name = "survey_id") }, inverseJoinColumns = {
			@JoinColumn(name = "label_id") })
	private Set<Labels> labels = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "survey_viewer", joinColumns = { @JoinColumn(name = "survey_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<User> viewers = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER ,cascade = { CascadeType.ALL })
	@JoinTable(name = "survey_question", joinColumns = { @JoinColumn(name = "survey_id") }, inverseJoinColumns = {
			@JoinColumn(name = "ques_id") })
	private Set<Questions> questions = new HashSet<>();
	
	@ManyToMany(mappedBy = "filledSurveyList")
	private Set<User> respondersList = new HashSet<>();

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Set<Labels> getLabels() {
		return labels;
	}

	public void setLabels(Set<Labels> labels) {
		this.labels = labels;
	}

	public Set<User> getViewers() {
		return viewers;
	}

	public void setViewers(Set<User> viewers) {
		this.viewers = viewers;
	}

	public Set<Questions> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Questions> questions) {
		this.questions = questions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + surveyId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Survey other = (Survey) obj;
		if (surveyId != other.surveyId)
			return false;
		return true;
	}

	public User getSurveyOwner() {
		return surveyOwner;
	}

	public void setSurveyOwner(User surveyOwner) {
		this.surveyOwner = surveyOwner;
	}

	public Set<User> getRespondersList() {
		return respondersList;
	}

	public void setRespondersList(Set<User> respondersList) {
		this.respondersList = respondersList;
	}	
}
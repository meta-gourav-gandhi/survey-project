package com.metacube.wesurve.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="labels")
public class Labels {

	@Id
	@Column(name = "label_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labelId;
	
	
	
	@Column(name = "label_name", length = 50, nullable = false)
	private String label_name;

	
	 @ManyToMany(mappedBy = "labels")
	 private Set<Survey> survey = new HashSet<>();


	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}



	public String getLabel_name() {
		return label_name;
	}



	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public Set<Survey> getSurvey() {
		return survey;
	}

	public void setSurvey(Set<Survey> survey) {
		this.survey = survey;
	}

	
	
	
	
}

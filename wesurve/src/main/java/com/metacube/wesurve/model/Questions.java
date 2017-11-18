/**
 * The class Questions is a POJO class for questionstb table.
 */
package com.metacube.wesurve.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questionstb")
public class Questions {

	@Id
	@Column(name = "ques_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quesId;

	@Column(name = "question", length = 500, nullable = false)
	private String question;

	@Column(name = "created_date", nullable = true)
	private Date createdDate;

	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;

	@Column(name = "required", nullable = false)
	private boolean required;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "question_option", joinColumns = { @JoinColumn(name = "ques_id") }, inverseJoinColumns = {
			@JoinColumn(name = "option_id") })
	private Set<Options> options = new HashSet<>();

	public Set<Options> getOptions() {
		return options;
	}

	public void setOptions(Set<Options> options) {
		this.options = options;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
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

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
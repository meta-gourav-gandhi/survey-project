package com.metacube.wesurve.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(UserResonsesID.class)
@Table(name = "user_responses")
public class UserResponses {
	
	@Id
	@Column(name = "user_id")
	private int userId;

	@Id
	@Column(name = "ques_id")
	private int quesId;
	
	@ManyToOne
	@JoinColumn(name = "option_id")
	private QuestionOptions optionId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public QuestionOptions getOptionId() {
		return optionId;
	}

	public void setOptionId(QuestionOptions optionId) {
		this.optionId = optionId;
	}
}
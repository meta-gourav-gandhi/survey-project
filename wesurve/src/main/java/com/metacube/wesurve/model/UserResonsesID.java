package com.metacube.wesurve.model;

import java.io.Serializable;

public class UserResonsesID implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private int quesId;

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
}
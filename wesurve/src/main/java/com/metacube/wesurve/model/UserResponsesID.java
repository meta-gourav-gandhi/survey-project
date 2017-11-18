/**
 * The class UserResponsesID is a composite key class for UserResponses class.
 */
package com.metacube.wesurve.model;

import java.io.Serializable;

public class UserResponsesID implements Serializable {

	private static final long serialVersionUID = 1L;
	private User user;
	private Questions question;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questions getQuestion() {
		return question;
	}

	public void setQuestion(Questions question) {
		this.question = question;
	}
}
/**
 * DTO class
 */
package com.metacube.wesurve.dto;

import java.util.Set;

public class QuestionDto {
	private int id;
	private String text;
	private Set<OptionDto> options;
	private boolean required;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<OptionDto> getOptions() {
		return options;
	}

	public void setOptions(Set<OptionDto> options) {
		this.options = options;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
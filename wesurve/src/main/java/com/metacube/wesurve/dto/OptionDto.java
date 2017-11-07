package com.metacube.wesurve.dto;

import com.metacube.wesurve.enums.OptionType;

public class OptionDto {
	private int id;
	private String text;
	private OptionType type;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public OptionType getType() {
		return type;
	}
	
	public void setType(OptionType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
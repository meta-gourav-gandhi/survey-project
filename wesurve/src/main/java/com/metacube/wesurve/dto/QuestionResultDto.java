package com.metacube.wesurve.dto;

import java.util.Map;

public class QuestionResultDto {
	private int id;
	private Map<Integer, Double> optionData; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, Double> getOptionData() {
		return optionData;
	}

	public void setOptionData(Map<Integer, Double> optionData) {
		this.optionData = optionData;
	}
}
/**
 * DTO class
 */
package com.metacube.wesurve.dto;

import java.util.List;

public class QuestionResultDto {
	private int id;
	private List<String> option;
	private List<Double> data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getOption() {
		return option;
	}

	public void setOption(List<String> option) {
		this.option = option;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}
}
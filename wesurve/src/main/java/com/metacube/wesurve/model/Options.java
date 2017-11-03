package com.metacube.wesurve.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.metacube.wesurve.enums.OptionType;

@Entity
@Table(name = "optionstb")
public class Options {

	@Id
	@Column(name = "option_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int optionId;

	
	@Column(name = "option_value ", length = 500, nullable = false)
	private String optionValue;

	@Enumerated(EnumType.STRING)
	private OptionType optionType;

	@Column(name = "created_date", nullable = true)
	private Date createdDate;

	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}


	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
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
}
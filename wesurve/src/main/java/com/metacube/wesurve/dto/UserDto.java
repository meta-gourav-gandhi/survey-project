package com.metacube.wesurve.dto;

import java.util.Date;

public class UserDto {
	private String name;
	private String email;
	private String password;
	private Date dob;
	private char gender;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getDob() {
		return dob;
	}
	
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public char getGender() {
		return gender;
	}
	
	public void setGender(char gender) {
		this.gender = gender;
	}
}
package com.metacube.wesurve.model;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Check;

@Entity
@Table(name="user_details")
public class User
{
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int userId;

	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email;

	@Column(name = "password", length = 128, nullable = true)
	private String password;

	@Column(name = "token", length = 32, nullable = true)
	private String token;

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "dob", nullable = true)
	private Date dob;
	
	@Column(name = "gender", length = 1, nullable = true)
	@Check(constraints = "gender IN ('M', 'F', 'O')")
	private char gender;
	
	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, columnDefinition = "int default 3")
	private UserRole userRole;
	
	@Column(name = "created_date", nullable = true)
	private Date createdDate;
	
	@Column(name = "updated_date", nullable = true)
	private Date updatedDate;
	
	@ManyToMany(mappedBy = "viewers")
	private Set<Survey> survey = new HashSet<>();

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<Survey> getSurvey() {
		return survey;
	}

	public void setSurvey(Set<Survey> survey) {
		this.survey = survey;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	@PrePersist
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	@PreUpdate
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
package com.metacube.wesurve.dto;

import com.metacube.wesurve.enums.Status;

public class ResponseDto <T> {
	private Status status;
	private T body;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
}
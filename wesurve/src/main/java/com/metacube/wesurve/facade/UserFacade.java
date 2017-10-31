package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;

public interface UserFacade {
	public Status createNewUser(UserDto userDetailsDto);
	public void sendEmail(String from, String password, String to, String subject, String body);
	public LoginResponseDto login(LoginCredentialsDto loginDto);
}
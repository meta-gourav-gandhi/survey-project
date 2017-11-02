package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;

public interface UserFacade {
	public Status createNewUser(UserDto userDetailsDto);
	public LoginResponseDto login(LoginCredentialsDto loginDto);
	public LoginResponseDto socialLogin(UserDto socialLoginCredentials);
	public Status forgotPassword(String email);
	public Iterable<UserDetailsDto> getAllUsers(String accessToken);
	public Status logout(String accessToken);
	public Role checkAuthorization(String accessToken);
	public Status changeUserRole(String token, String email);
}
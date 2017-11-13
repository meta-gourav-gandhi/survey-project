package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;

public interface UserFacade {
	public ResponseDto<Void> createNewUser(UserDto userDetailsDto);
	public ResponseDto<LoginResponseDto> login(LoginCredentialsDto loginDto);
	public ResponseDto<LoginResponseDto> socialLogin(UserDto socialLoginCredentials);
	public ResponseDto<Void> forgotPassword(String email);
	public Iterable<UserDetailsDto> getAllUsers(String accessToken);
	public Status logout(String accessToken);
	public Role checkAuthorization(String accessToken);
	public Status changeUserRole(int userId);
	public Iterable<SurveyInfoDto> getSurveyListForViewers(String accessToken);
	public ResponseDto<Void> changePassword(String accessToken, String currentPassword, String newPassword);
	public Iterable<SurveyInfoDto> getSurveyListOfSurveyor(String accessToken);
	public Iterable<SurveyInfoDto> getListOfFilledSurveys(String accessToken);
}
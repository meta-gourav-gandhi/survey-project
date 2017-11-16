package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDetailsForSurveyorDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;

public interface UserFacade {
	ResponseDto<Void> createNewUser(UserDto userDetailsDto);
	ResponseDto<LoginResponseDto> login(LoginCredentialsDto loginDto);
	ResponseDto<LoginResponseDto> socialLogin(UserDto socialLoginCredentials);
	ResponseDto<Void> forgotPassword(String email);
	Iterable<UserDetailsDto> getAllUsers(String accessToken);
	Status logout(String accessToken);
	Role checkAuthorization(String accessToken);
	Status changeUserRole(int userId);
	Iterable<SurveyInfoDto> getSurveyListForViewers(String accessToken);
	ResponseDto<Void> changePassword(String accessToken, String currentPassword, String newPassword);
	Iterable<SurveyInfoDto> getSurveyListOfSurveyor(String accessToken);
	Iterable<SurveyInfoDto> getListOfFilledSurveys(String accessToken);
	Iterable<UserDetailsForSurveyorDto> getAllUsersForSurveyor(String accessToken, int surveyId);
}
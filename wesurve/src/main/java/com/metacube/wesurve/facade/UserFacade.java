package com.metacube.wesurve.facade;

import com.metacube.wesurve.authorize.UserData;
import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDetailsForSurveyorDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;

public interface UserFacade {
	ResponseDto<Void> createNewUser(UserDto userDetailsDto);
	ResponseDto<LoginResponseDto> login(LoginCredentialsDto loginDto);
	ResponseDto<LoginResponseDto> socialLogin(UserDto socialLoginCredentials);
	ResponseDto<Void> forgotPassword(String email);
	Iterable<UserDetailsDto> getAllUsers(int userId);
	Status logout(int userId);
	UserData checkAuthorization(String accessToken);
	Status changeUserRole(int userId);
	Iterable<SurveyInfoDto> getSurveyListForViewers(int userId);
	ResponseDto<Void> changePassword(int userId, String currentPassword, String newPassword);
	Iterable<SurveyInfoDto> getSurveyListOfSurveyor(int userId);
	Iterable<SurveyInfoDto> getListOfFilledSurveys(int userId);
	Iterable<UserDetailsForSurveyorDto> getAllUsersForSurveyor(int surveyorId, int surveyId);
}
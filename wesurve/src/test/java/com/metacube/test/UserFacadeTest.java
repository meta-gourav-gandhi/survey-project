package com.metacube.test;



import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metacube.wesurve.dto.LoginCredentialsDto;
import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.UserFacade;
import com.metacube.wesurve.service.UserService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserFacadeTest {

	@Autowired
	UserFacade userFacade;
	
	
	static final String USERNAME = "Gourav";
	static final String PASSWORD = "12345678";
	static final String EMAIL = "gourav.gandhi@metacube.com";
	static final int ID = 1; 
	static final int TEMPUSERID = 2;
	static final int SOCIALUSERID = 3;
	
	
	
	@Test
	public void test01_CreateUserInvalidCredentialName() {
		
		UserDto userDto =new  UserDto();
		
		userDto.setGender("Male");
		userDto.setEmail(EMAIL);
		userDto.setDob("2017-11-17");
		userDto.setPassword(PASSWORD);
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.INVALID_CONTENT;
		
		assertEquals(expected.name(), response.getStatus().name());
		

	}
	
	
	@Test
	public void test01_CreateUserInvalidCredentialEmail() {
		
		UserDto userDto =new  UserDto();
		userDto.setName(USERNAME);
		userDto.setGender("Male");
		userDto.setEmail("gourav.gandhi");
		userDto.setDob("2017-11-17");
		userDto.setPassword(PASSWORD);
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.INVALID_CONTENT;
		
		assertEquals(expected.name(), response.getStatus().name());
		

	}
	
	
	@Test
	public void test01_CreateUserInvalidCredentialPassword() {
		
		UserDto userDto =new  UserDto();
		userDto.setName(USERNAME);
		userDto.setGender("Male");
		userDto.setEmail("gourav.gandhi");
		userDto.setDob("2017-11-17");
		userDto.setPassword("1234");
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.INVALID_CONTENT;
		
		assertEquals(expected.name(), response.getStatus().name());	

	}
	
	
	@Test
	public void test02_PositiveCreateUser() {
		
		UserDto userDto =new  UserDto();
		
		userDto.setName(USERNAME);
		userDto.setGender("Male");
		userDto.setEmail(EMAIL);
		userDto.setDob("2017-11-17");
		userDto.setPassword(PASSWORD);
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.SUCCESS;
		
		assertEquals(expected.name(), response.getStatus().name());
		

	}
	
	@Test
	public void test02_PositiveCreateUser2() {
		
		UserDto userDto =new  UserDto();
		
		userDto.setName("Demo User");
		userDto.setGender("Male");
		userDto.setEmail("demo@gmail.com");
		userDto.setDob("2017-11-17");
		userDto.setPassword("12345678");
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.SUCCESS;
		
		assertEquals(expected.name(), response.getStatus().name());
		

	}
	
	@Test
	public void test03_CreateUserWithDuplicateEmail() {
		
		UserDto userDto =new  UserDto();
		
		userDto.setName("GouravDuplicate");
		userDto.setGender("Male");
		userDto.setEmail(EMAIL);
		userDto.setDob("2017-11-17");
		userDto.setPassword("123456789");
		
		ResponseDto<Void> response = new ResponseDto<>();
		response = userFacade.createNewUser(userDto);
		Status expected = Status.DUPLICATE;
		
		assertEquals(expected.name(), response.getStatus().name());
		

	}
	
	
	
	
	@Test
	public void test11_LoginWithInvalidCredentialEmail() {
		
		
		LoginCredentialsDto loginCredential = new LoginCredentialsDto();
		loginCredential.setEmail("gourav");
		loginCredential.setPassword(PASSWORD);
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		response = userFacade.login(loginCredential);
		assertEquals(Status.INVALID.name(), response.getStatus().name());
		
	}
	
	
	@Test
	public void test12_LoginWithInvalidCredentialPassword() {
		
		
		LoginCredentialsDto loginCredential = new LoginCredentialsDto();
		loginCredential.setEmail(EMAIL);
		loginCredential.setPassword("1234567890");
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		response = userFacade.login(loginCredential);
		assertEquals(Status.INVALID.name(), response.getStatus().name());
		
	}
	
	
	@Test
	public void test13_LoginWithValidCredential() {
		
		
		LoginCredentialsDto loginCredential = new LoginCredentialsDto();
		loginCredential.setEmail(EMAIL);
		loginCredential.setPassword(PASSWORD);
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		response = userFacade.login(loginCredential);
		
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		
		loginResponseDto.setName(USERNAME);
		loginResponseDto.setRole(3);
		
		assertEquals(loginResponseDto.getName() , response.getBody().getName());
		assertEquals(loginResponseDto.getRole() , response.getBody().getRole());
		
	}
	

	
	
	@Test
	public void test21_SocialLoginWithInValidCredential() {
			
		UserDto socialLoginCredentials = new UserDto();
		socialLoginCredentials.setName(USERNAME);
		socialLoginCredentials.setEmail("gourav");
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		
		response = userFacade.socialLogin(socialLoginCredentials);
		
		assertEquals(Status.INVALID.name() , response.getStatus().name());
			
	}
	
	@Test
	public void test22_SocialLoginWithValidCredentialPreviousUser() {
			
		UserDto socialLoginCredentials = new UserDto();
		socialLoginCredentials.setName(USERNAME);
		socialLoginCredentials.setEmail(EMAIL);
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		
		response = userFacade.socialLogin(socialLoginCredentials);
		
		assertEquals(Status.SUCCESS.name() , response.getStatus().name());
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		
		loginResponseDto.setName(USERNAME);
		loginResponseDto.setRole(3);
		
		assertEquals(loginResponseDto.getName() , response.getBody().getName());
		assertEquals(loginResponseDto.getRole() , response.getBody().getRole());
		
		
			
	}
	
	@Test
	public void test22_SocialLoginWithValidCredentialNewUser() {
			
		UserDto socialLoginCredentials = new UserDto();
		socialLoginCredentials.setName("GouravSocial");
		socialLoginCredentials.setEmail("gouravgandhi23195@gmail.com");
		
		ResponseDto<LoginResponseDto> response = new ResponseDto<>();
		
		response = userFacade.socialLogin(socialLoginCredentials);
		
		assertEquals(Status.SUCCESS.name() , response.getStatus().name());
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		
		loginResponseDto.setName("GouravSocial");
		loginResponseDto.setRole(3);
		
		assertEquals(loginResponseDto.getName() , response.getBody().getName());
		assertEquals(loginResponseDto.getRole() , response.getBody().getRole());
		
		
			
	}
	
	@Test
	public void test31_GetAllUser() {
		
		Iterable<UserDetailsDto> userDetailsDtoSet  = userFacade.getAllUsers(ID);
		Set<UserDetailsDto> userDetails = new HashSet<>();
		
		for(UserDetailsDto currUserDetails : userDetailsDtoSet ) {
			userDetails.add(currUserDetails);
		}
		
		UserDetailsDto expectedUser = new UserDetailsDto();
		
		expectedUser.setRole(3);
		expectedUser.setName("GouravSocial");
		expectedUser.setEmail("gouravgandhi23195@gmail.com");
		expectedUser.setId(SOCIALUSERID);
		
		assertTrue(userDetails.contains(expectedUser));
		
	}
	
	@Test
	public void test32_GetAllUserNegativeTest() {
		
		Iterable<UserDetailsDto> userDetailsDtoSet  = userFacade.getAllUsers(ID);
		Set<UserDetailsDto> userDetails = new HashSet<>();
		
		for(UserDetailsDto currUserDetails : userDetailsDtoSet ) {
			userDetails.add(currUserDetails);
		}
		
		UserDetailsDto expectedUser = new UserDetailsDto();
		
		expectedUser.setRole(3);
		expectedUser.setName("GouravSocial");
		expectedUser.setEmail("gouravgandhi23195@gmail.com");
		expectedUser.setId(100);
		
		assertFalse(userDetails.contains(expectedUser));
		
		
		
		
	}
	
	
	@Test
	public void test41_Logout() {
		
		Status status = userFacade.logout(ID);
		assertEquals(Status.SUCCESS.name() , status.name());
		
	}
	
	@Test 
	public void test51_ChangeUserRoleToSurveyor() {
		
		Status status = userFacade.changeUserRole(SOCIALUSERID);
		assertEquals(Status.SUCCESS.name(), status.name());
		
	}
	
	@Test 
	public void test52_ChangeUserRoleToUser() {
		
		Status status = userFacade.changeUserRole(SOCIALUSERID);
		assertEquals(Status.SUCCESS.name(), status.name());
		
	}
	
	@Test 
	public void test53_ChangeUserRoleOfInvalidUser() {
		
		Status status = userFacade.changeUserRole(10);
		assertEquals(Status.FAILURE.name(), status.name());
		
	}
	
	
	@Test
	public void test61_ChangePasswordSameNewPassword() {
		
		ResponseDto<Void> response = userFacade.changePassword(TEMPUSERID, "12345678", "12345678");
		assertEquals(Status.DUPLICATE.name() , response.getStatus().name());
		
	}
	
	@Test
	public void test62_ChangePasswordInvalidNewPassword() {
		
		ResponseDto<Void> response = userFacade.changePassword(TEMPUSERID, "12345678", "123456");
		assertEquals(Status.INVALID_CONTENT.name() , response.getStatus().name());
		
	}
	
	@Test
	public void test63_ChangePasswordInvalidUser() {
		
		ResponseDto<Void> response = userFacade.changePassword(100, "12345678", "87654321");
		assertEquals(Status.FAILURE.name() , response.getStatus().name());
		
	}
	
	
	@Test
	public void test64_ChangePasswordPositive() {
		
		ResponseDto<Void> response = userFacade.changePassword(TEMPUSERID, "12345678", "87654321");
		assertEquals(Status.SUCCESS.name() , response.getStatus().name());
		
	}
	
	@Test
	public void test64_ChangePasswordPositive2() {
		
		ResponseDto<Void> response = userFacade.changePassword(TEMPUSERID, "87654321", "12345678");
		assertEquals(Status.SUCCESS.name() , response.getStatus().name());
		
	}
	
	
	
	
	
	
	
	


}
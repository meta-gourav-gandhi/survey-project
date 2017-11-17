package com.metacube.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.facade.UserFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
public class UserFacadeTest {

	@Autowired
	UserFacade userFacade;
	
	@Test
	public void testWelcome() {
		
		assertTrue(true);

	}
	
	@Test
	public void Test1_PositiveTestCasecreateNewUser() {
		UserDto userDto = new UserDto();
		userDto.setName("Priya");
		userDto.setEmail("priya.mishra@metacube.com");
		userDto.setGender("F");
		userDto.setPassword("12345678");
		userDto.setDob("2017-10-19");
		userFacade.createNewUser(userDto);
	}
}
package com.metacube.wesurve.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.UserDetails;
import com.metacube.wesurve.service.UserDetailsService;


@Component("userDetailsFacade")
public class DefaultUserDetailsFacade implements UserDetailsFacade
{
	@Autowired
	UserDetailsService userDetailsService;
	
	public DefaultUserDetailsFacade() {
		
	}

	@Override
	public Status createNewUser(UserDetailsDto userDetailsDto) {
		Status status = Status.FAILURE;
		UserDetails userDetails = convertDtoToModel(userDetailsDto);
		if(userDetailsService.createNewUser(userDetails) != null) {
			status = Status.SUCCESS;
		}
		return status;
	}

	private UserDetails convertDtoToModel(UserDetailsDto userDetailsDto) {
		UserDetails userDetails = new UserDetails();
		userDetails.setName(userDetailsDto.getName());
		userDetails.setDob(userDetailsDto.getDob());
		userDetails.setEmail(userDetailsDto.getEmail());
		userDetails.setGender(userDetailsDto.getGender());
		userDetails.setPassword(userDetailsDto.getPassword());
		return userDetails;
	}
}
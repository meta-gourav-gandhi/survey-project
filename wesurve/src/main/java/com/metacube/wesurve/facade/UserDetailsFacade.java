package com.metacube.wesurve.facade;

import com.metacube.wesurve.dto.UserDetailsDto;
import com.metacube.wesurve.enums.Status;

public interface UserDetailsFacade {
	public Status createNewUser(UserDetailsDto userDetailsDto);
}
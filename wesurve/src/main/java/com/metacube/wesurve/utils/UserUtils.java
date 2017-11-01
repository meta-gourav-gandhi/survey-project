package com.metacube.wesurve.utils;

import com.metacube.wesurve.dto.LoginResponseDto;
import com.metacube.wesurve.enums.Roles;

public class UserUtils {

	static public Roles checkAuthorization(String token, LoginResponseDto loginResponseDto) {
		Roles role = Roles.INVALID;
		if (loginResponseDto != null && token != null) {
			if (loginResponseDto.getAccessToken().equals(token)) {
				switch (loginResponseDto.getRole()) {
				case 1:
					role = Roles.ADMIN;
					break;

				case 2:
					role = Roles.SURVEYOR;
					break;

				case 3:
					role = Roles.USER;
					break;
				}
			}
		}

		return role;
	}
}

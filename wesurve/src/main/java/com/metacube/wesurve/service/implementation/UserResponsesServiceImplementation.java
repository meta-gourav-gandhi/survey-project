package com.metacube.wesurve.service.implementation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.userresponse.UserResponsesDao;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;
import com.metacube.wesurve.service.UserResponsesService;

@Service("userResponsesService")
public class UserResponsesServiceImplementation implements UserResponsesService {

	@Resource(name = "hibernateUserResponsesDaoImplementation")
	UserResponsesDao userResponsesDao;

	@Override
	public Status addNewResponse(UserResponses userResponse) {
		Status status;
		try {
			UserResponses savedUserResponse = userResponsesDao.save(userResponse);
			if (savedUserResponse != null) {
				status = Status.SUCCESS;
			} else {
				status = Status.FAILURE;
			}
		} catch (Exception exception) {
			status = Status.FAILURE;
			exception.printStackTrace();
		}

		return status;
	}

	@Override
	public Double getUserResponsesOfAQuestionAndOption(Questions curQues, Options curOption) {
		List<UserResponses> responses = userResponsesDao.userResponsesByQuestionAndOption(curQues, curOption);
		return (double) responses.size();
	}

	@Override
	public UserResponses getUserResponseById(User user, Questions question) {
		UserResponses userResponse = null;
		try {
			userResponse = userResponsesDao.findById(user, question);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return userResponse;
	}

}
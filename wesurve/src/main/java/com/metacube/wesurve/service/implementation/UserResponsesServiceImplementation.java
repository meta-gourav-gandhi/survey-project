/**
 * The UserResponsesServiceImplementation class is service class for UserResponses Model.
 */
package com.metacube.wesurve.service.implementation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.UserResponsesDao;
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
	
	/**
	 * This method saves new userResponse.
	 * @param userResponse
	 * @return Status
	 */
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

	/**
	 * @param curQues
	 * @param curOption
	 * @return Double
	 */
	@Override
	public Double getUserResponsesOfAQuestionAndOption(Questions ques, Options option) {
		List<UserResponses> responses;
		try {
			responses = userResponsesDao.userResponsesByQuestionAndOption(ques, option);
		} catch(Exception exception) {
			exception.printStackTrace();
			responses = null;
		}
		
		return (double) responses.size();
	}

	/**
	 * This method returns UserResponses by the given IDs (composite key)
	 * @param user - User object
	 * @param question - Question object
	 * @return UserResponses object
	 */
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
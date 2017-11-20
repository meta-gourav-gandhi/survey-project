/**
 * The UserResponsesService interface is service layer interface UserResponses Model 
 */
package com.metacube.wesurve.service;

import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;

public interface UserResponsesService {
	Status addNewResponse(UserResponses userResponse);
	UserResponses getUserResponseById(User userId, Questions quesId);
	Double getUserResponsesOfAQuestionAndOption(Questions curQues, Options curOption);
}
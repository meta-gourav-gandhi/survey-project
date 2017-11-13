package com.metacube.wesurve.dao.userresponse;

import java.util.List;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;

public interface UserResponsesDao extends AbstractDao<UserResponses, Integer> {
	UserResponses findById(User user, Questions question);
	List<UserResponses> userResponsesByQuestionAndOption(Questions curQues, Options curOption);
}
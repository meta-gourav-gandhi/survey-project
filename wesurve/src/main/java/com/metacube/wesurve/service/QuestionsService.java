/**
 * The QuestionsService interface is service layer interface Questions Model 
 */
package com.metacube.wesurve.service;

import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;

public interface QuestionsService {
	Questions getQuestionById(int quesId);
	Options getOptionById(int optionId);
}
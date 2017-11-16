package com.metacube.wesurve.service.implementation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.options.OptionsDao;
import com.metacube.wesurve.dao.questions.QuestionsDao;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.service.QuestionsService;

@Service("questionsService")
public class QuestionServiceImplementation implements QuestionsService {
	@Resource(name = "hibernateQuestionsDaoImplementation")
	QuestionsDao questionsDao;
	
	@Resource(name = "hibernateOptionsDaoImplementation")
	OptionsDao optionsDao;

	@Override
	public Questions getQuestionById(int quesId) {
		Questions question;
		try {
			question = questionsDao.findOne(quesId);
		} catch (Exception exception) {
			question = null;
			exception.printStackTrace();
		}
		
		return question;
	}

	@Override
	public Options getOptionById(int optionId) {
		Options option;
		try {
			option = optionsDao.findOne(optionId);
		} catch (Exception exception) {
			option = null;
			exception.printStackTrace();
		}
		
		return option;
	}
}
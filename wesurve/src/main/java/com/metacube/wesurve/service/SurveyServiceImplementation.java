package com.metacube.wesurve.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.survey.SurveyDao;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Survey;

@Service("surveyService")
@Transactional
public class SurveyServiceImplementation implements SurveyService {

	@Resource(name = "hibernateSurveyDaoImplementation")
	SurveyDao surveyDao;
	
	@Override
	public Survey createSurvey(Survey survey) {
		return surveyDao.save(survey);
	}

	@Override
	public void changeSurveyStatus(Survey survey, SurveyStatus status) {
		if(survey.getSurveyStatus() != SurveyStatus.DELETED) {
			survey.setSurveyStatus(status);
			surveyDao.update(survey);
		}
	}

	@Override
	public Survey getSurveyById(int surveyId) {
		return surveyDao.findOne(surveyId);
	}

	@Override
	public void deleteSurvey(Survey survey) {
		surveyDao.delete(survey);
	}
}
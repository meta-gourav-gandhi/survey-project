package com.metacube.wesurve.service.implementation;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.survey.SurveyDao;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.SurveyService;
import com.metacube.wesurve.utils.Constants;

@Service("surveyService")
public class SurveyServiceImplementation implements SurveyService {

	@Resource(name = "hibernateSurveyDaoImplementation")
	SurveyDao surveyDao;
	
	@Override
	public Survey createSurvey(Survey survey) {
		Survey newSurvey;
		try {
			newSurvey = surveyDao.save(survey);
		} catch (Exception exception) {
			newSurvey = null;
			exception.printStackTrace();
		}
		
		return newSurvey;
	}

	@Override
	public Status changeSurveyStatus(Survey survey, SurveyStatus status) {
		Status changeStatus;
		try {
			if(survey.getSurveyStatus() != SurveyStatus.DELETED) {
				survey.setSurveyStatus(status);
				surveyDao.update(survey);
			}
			changeStatus = Status.SUCCESS;
		} catch (Exception exception) {
			exception.printStackTrace();
			changeStatus = Status.FAILURE;
		}
		
		return changeStatus;
	}

	@Override
	public Survey getSurveyById(int surveyId) {
		Survey survey;
		try {
			survey = surveyDao.findOne(surveyId);
		} catch (Exception exception) {
			exception.printStackTrace();
			survey = null;
		}
		
		return survey;
		
	}

	@Override
	public Status deleteSurvey(Survey survey) {
		Status status;
		try {
			surveyDao.delete(survey);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			exception.printStackTrace();
			status = Status.FAILURE;
		}
		
		return status;
	}

	@Override
	public String getSurveyURL(Survey survey) {
		return Constants.SURVEYURLINITIALS + survey.getSurveyId() + "/";
	}

	@Override
	public Status edit(Survey survey) {
		Status status;
		try {
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status =Status.FAILURE;
			exception.printStackTrace();
		}
		
		return status;
	}

	@Override
	public Status addViewer(Survey survey, User viewer) {
		Status status;
		try {
			survey.getViewers().add(viewer);
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch(Exception exception) {
			status = Status.FAILURE;
		}
		
		return status;
	}

	@Override
	public Status removeViewer(Survey survey, User viewer) {
		Status status;
		try {
			survey.getViewers().remove(viewer);
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch(Exception exception) {
			status = Status.FAILURE;
		}
		
		return status;
	}
	
	@Override
    public Set<Survey> getMatchedSurveys(User surveyor, String searchString) {
   	 Set<Survey> matchedSurveys = new HashSet<>();
   	 
   	 Iterable<Survey> surveyList = surveyDao.getMatchedSurvey(surveyor, searchString);
   	 
   	 if(surveyList !=null) {
   		 for(Survey survey : surveyList) {
   			 
   			 if(survey.getSurveyStatus() != SurveyStatus.DELETED) {
   				 matchedSurveys.add(survey);
   			 }
   		 }
   	 }
   	 return matchedSurveys;
    }
}
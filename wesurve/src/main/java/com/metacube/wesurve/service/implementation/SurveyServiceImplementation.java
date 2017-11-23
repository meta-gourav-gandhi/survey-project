/**
 * The SurveyServiceImplementation class is service class for Survey Model.
 */
package com.metacube.wesurve.service.implementation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.SurveyDao;
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

	/**
	 * This method creates a new survey.
	 * @param survey
	 * @return Survey - created survey
	 */
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

	/**
	 * This method changes the survey status.
	 * @param survey - Survey object
	 * @param status - SurveyStatus
	 * @return Status
	 */
	@Override
	public Status changeSurveyStatus(Survey survey, SurveyStatus status) {
		Status changeStatus;
		try {
			if (survey.getSurveyStatus() != SurveyStatus.DELETED) {
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

	/**
	 * This method returns the Survey object with the survey id.
	 * @param surveyId
	 * @return Survey object
	 */
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

	/**
	 * This method deletes a survey.
	 * @param survey - Survey object
	 * @return Status 
	 */
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

	/**
	 * This method returns the survey URL.
	 * @param survey
	 * @return survey url - String
	 */
	@Override
	public String getSurveyURL(Survey survey) {
		return Constants.SURVEY_BASE_URL + survey.getSurveyId() + "/";
	}

	/**
	 * This method edits the survey
	 * @param survey - Survey object
	 * @return Status
	 */
	@Override
	public Status edit(Survey survey) {
		Status status;
		try {
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
			exception.printStackTrace();
		}

		return status;
	}
	
	/**
	 * This method adds new viewer of survey result.
	 * @param survey - Survey object
	 * @param viewer - User object
	 * @return Status.
	 */
	@Override
	public Status addViewer(Survey survey, User viewer) {
		Status status;
		try {
			survey.getViewers().add(viewer);
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
		}

		return status;
	}

	/**
	 * This method removes viewer from the survey view list.
	 * @param survey - Survey object
	 * @param viewer - User object
	 * @return Status
	 */
	@Override
	public Status removeViewer(Survey survey, User viewer) {
		Status status;
		try {
			survey.getViewers().remove(viewer);
			surveyDao.update(survey);
			status = Status.SUCCESS;
		} catch (Exception exception) {
			status = Status.FAILURE;
		}

		return status;
	}
}
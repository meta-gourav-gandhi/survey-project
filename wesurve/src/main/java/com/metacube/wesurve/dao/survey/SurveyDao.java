package com.metacube.wesurve.dao.survey;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;

public interface SurveyDao extends AbstractDao<Survey, Integer>{

	Iterable<Survey> getMatchedSurvey(User surveyor, String searchString);

}
/**
 * The HibernateSurveyDaoImplementation class is a DAO class for Survey Model.
 * It extends GenericHibernateDao class and implements SurveyDao interface.
 */
package com.metacube.wesurve.dao.survey;

import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Survey;

@Repository("hibernateSurveyDaoImplementation")
public class HibernateSurveyDaoImplementation extends GenericHibernateDao<Survey, Integer> implements SurveyDao {

	public HibernateSurveyDaoImplementation() {
		super(Survey.class);
	}

	@Override
	public String getPrimaryKey() {
		return "surveyId";
	}
}
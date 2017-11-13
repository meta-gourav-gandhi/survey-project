package com.metacube.wesurve.dao.survey;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;

@Repository("hibernateSurveyDaoImplementation")
public class HibernateSurveyDaoImplementation extends GenericHibernateDao<Survey, Integer> implements SurveyDao {

	public HibernateSurveyDaoImplementation() {
		super(Survey.class);
	}

	@Override
	public String getPrimaryKey() {
		return "surveyId";
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public Iterable<Survey> getMatchedSurvey(User surveyor, String searchString) {
   	 Session session = getSessionFactory().getCurrentSession();
   	 Criteria criteria = session.createCriteria(Survey.class);
   	 criteria.add(Restrictions.eq("surveyOwner", surveyor)).add
   	 (Restrictions.like("surveyName", searchString , MatchMode.ANYWHERE));
   	 
   	 return criteria.list();
    }

}
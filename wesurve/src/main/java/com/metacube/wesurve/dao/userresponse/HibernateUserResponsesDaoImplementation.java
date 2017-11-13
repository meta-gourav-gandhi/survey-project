package com.metacube.wesurve.dao.userresponse;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;

@Repository("hibernateUserResponsesDaoImplementation")
public class HibernateUserResponsesDaoImplementation extends GenericHibernateDao<UserResponses, Integer>
		implements UserResponsesDao {

	public HibernateUserResponsesDaoImplementation() {
		super(UserResponses.class);
	}

	@Override
	public String getPrimaryKey() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserResponses> userResponsesByQuestionAndOption(Questions curQues, Options curOption) {

		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserResponses.class);
		criteria.add(Restrictions.eq("question", curQues)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("option", curOption)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public UserResponses findById(User user, Questions question) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserResponses.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("question", question));
		return (UserResponses) criteria.uniqueResult();
	}
}
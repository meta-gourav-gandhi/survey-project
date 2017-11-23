/**
 * The HibernateUserResponsesDaoImplementation class is a DAO class for UserResponses Model.
 * It extends GenericHibernateDao class and implements UserResponsesDao interface.
 */
package com.metacube.wesurve.dao.implementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.UserResponsesDao;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;

@Repository("hibernateUserResponsesDaoImplementation")
public class HibernateUserResponsesDaoImplementation extends GenericHibernateDao<UserResponses, Integer> implements UserResponsesDao {

	public HibernateUserResponsesDaoImplementation() {
		super(UserResponses.class);
	}

	@Override
	public String getPrimaryKey() {
		return null;
	}

	/**
	 * This method returns list of records that matches to the given question and option.
	 * @param question
	 * @param option
	 * @return List of UserResponses objects
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserResponses> userResponsesByQuestionAndOption(Questions question, Options option) {

		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserResponses.class);
		criteria.add(Restrictions.eq("question", question)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("option", option)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * This method finds the record by id(composite key)
	 * @param user
	 * @param question
	 * @return UserResponses object
	 */
	@Override
	public UserResponses findById(User user, Questions question) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserResponses.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("question", question));
		return (UserResponses) criteria.uniqueResult();
	}
}
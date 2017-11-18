/**
 * The HibernateUserDaoImplementation class is a DAO class for User Model.
 * It extends GenericHibernateDao class and implements UserDao interface.
 */
package com.metacube.wesurve.dao.userdetails;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.User;

@Repository("hibernateUserDaoImplementation")
public class HibernateUserDaoImplementation extends GenericHibernateDao<User, Integer> implements UserDao {
	public HibernateUserDaoImplementation() {
		super(User.class);
	}

	/**
	 * This methods checks if email exists in table.
	 * @param email
	 * @return boolean
	 */
	@Override
	public boolean checkIfEmailExists(String email) {
		boolean result = false;
		if (getUserByEmail(email) != null) {
			result = true;
		}

		return result;
	}

	
	/**
	 * This method authenticates the user.
	 * @param email
	 * @param password
	 * @return User object
	 */
	@Override
	public User authenticateUser(String email, String password) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email)).uniqueResult();
		User userDetails = (User) criteria.add(Restrictions.eq("password", password)).uniqueResult();
		return userDetails;
	}

	
	/**
	 * This method returns the user by the given email.
	 * @param email
	 * @return User object
	 */
	@Override
	public User getUserByEmail(String email) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		return (User) criteria.add(Restrictions.eq("email", email)).uniqueResult();
	}

	
	/**
	 * This method returns the user by access token.
	 * @param accessToken
	 * @return User object
	 */
	@Override
	public User getUserByAccessToken(String accessToken) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		return (User) criteria.add(Restrictions.eq("token", accessToken)).uniqueResult();
	}

	@Override
	public String getPrimaryKey() {
		return "userId";
	}
}
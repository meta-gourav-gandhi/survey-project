package com.metacube.wesurve.dao.userdetails;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.UserDetails;


@Repository("hibernateUserDetailsDao")
public class HibernateUserDetailsDao extends GenericHibernateDao<UserDetails, Integer> implements UserDetailsDao {
	public HibernateUserDetailsDao() {
		super(UserDetails.class);
	}

	@Override
	public boolean checkIfEmailExists(String email) {
		boolean result = false;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserDetails.class);
		UserDetails userDetails = (UserDetails) criteria.add(Restrictions.eq("email", email)).uniqueResult();
		if(userDetails != null) {
			result = true;
		}
		return result;
	}
}

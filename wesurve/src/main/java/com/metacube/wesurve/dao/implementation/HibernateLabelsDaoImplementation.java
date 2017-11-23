/**
 * The HibernateLabelsDaoImplementation class is a DAO class for Labels Model.
 * It extends GenericHibernateDao class and implements LabelsDao interface.
 */
package com.metacube.wesurve.dao.implementation;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.LabelsDao;
import com.metacube.wesurve.model.Labels;

@Repository("hibernateLabelsDaoImplementation")
public class HibernateLabelsDaoImplementation extends GenericHibernateDao<Labels, Integer> implements LabelsDao {

	public HibernateLabelsDaoImplementation() {
		super(Labels.class);
	}

	@Override
	public String getPrimaryKey() {
		return "labelId";
	}
	
	/**
	 * This method finds Label by name.
	 * @param label
	 * @return Labels class object
	 */
	@Override
	public Labels getByLabelName(String label) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Labels.class);
		return (Labels) criteria.add(Restrictions.eq("labelName", label)).uniqueResult();
	}
}
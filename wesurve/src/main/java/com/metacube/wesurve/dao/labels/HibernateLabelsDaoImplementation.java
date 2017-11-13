package com.metacube.wesurve.dao.labels;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Labels;


@Repository("hibernateLabelsDaoImplementation")
public class HibernateLabelsDaoImplementation extends GenericHibernateDao<Labels, Integer> implements LabelsDao{

	public HibernateLabelsDaoImplementation() {
		super(Labels.class);
	}

	@Override
	public String getPrimaryKey() {
		return "labelId";
	}

	@Override
	public Labels getByLabelName(String label) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Labels.class);
		return (Labels) criteria.add(Restrictions.eq("labelName", label)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public Iterable<Labels> getLabelsByNameAlike(String searchString) {
   	 Session session = getSessionFactory().getCurrentSession();
   	 Criteria criteria = session.createCriteria(Labels.class);
   	 criteria.add(Restrictions.like("labelName", searchString , MatchMode.ANYWHERE));
   	 return criteria.list();
    }

}
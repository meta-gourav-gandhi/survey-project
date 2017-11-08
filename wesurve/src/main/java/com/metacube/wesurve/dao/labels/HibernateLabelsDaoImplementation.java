package com.metacube.wesurve.dao.labels;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Survey;


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
//		List<Labels> list = criteria.list();
//		Iterator<Labels> iter = list.iterator();
//		while(iter.hasNext()) {
//			Labels l = iter.next();
//			System.out.println("................................");
//			System.out.println("Label ID: " + l.getLabelId());
//			System.out.println("Label Name: " + l.getLabelName());
//			System.out.println("Has these surveys:");
//			Iterator<Survey> i1 = l.getSurvey().iterator();
//			while(i1.hasNext()) {
//				System.out.println(i1.next().getSurveyName());
//			}
//			System.out.println("..............................................................");
//		}
		System.out.println("///////////////////////////////////////////////////////////////////////////////");
		List<Labels> newlist = criteria.add(Restrictions.eq("labelName", label)).list();
		
		for(Labels obj : newlist) {
			Set<Survey> curobj =  obj.getSurvey();
			for(Survey object : curobj) {
				System.out.println(object.getSurveyName());
			}
		}
		
		
		return null;
	}
}
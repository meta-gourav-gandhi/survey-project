package com.metacube.wesurve.dao.labels;

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
}
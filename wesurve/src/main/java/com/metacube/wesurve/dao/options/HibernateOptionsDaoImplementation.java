/**
 * The HibernateOptionsDaoImplementation class is a DAO class for Labels Model.
 * It extends GenericHibernateDao class and implements OptionsDao interface.
 */
package com.metacube.wesurve.dao.options;

import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Options;

@Repository("hibernateOptionsDaoImplementation")
public class HibernateOptionsDaoImplementation extends GenericHibernateDao<Options, Integer> implements OptionsDao {

	public HibernateOptionsDaoImplementation() {
		super(Options.class);
	}

	@Override
	public String getPrimaryKey() {
		return "optionId";
	}
}
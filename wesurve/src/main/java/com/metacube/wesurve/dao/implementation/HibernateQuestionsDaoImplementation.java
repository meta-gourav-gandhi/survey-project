/**
 * The HibernateQuestionsDaoImplementation class is a DAO class for Questions Model.
 * It extends GenericHibernateDao class and implements QuestionsDao interface.
 */
package com.metacube.wesurve.dao.implementation;

import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.QuestionsDao;
import com.metacube.wesurve.model.Questions;

@Repository("hibernateQuestionsDaoImplementation")
public class HibernateQuestionsDaoImplementation extends GenericHibernateDao<Questions, Integer>
		implements QuestionsDao {

	public HibernateQuestionsDaoImplementation() {
		super(Questions.class);
	}

	@Override
	public String getPrimaryKey() {
		return "quesId";
	}
}
package com.metacube.wesurve.dao.questions;

import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.Questions;

@Repository("hibernateQuestionsDaoImplementation")
public class HibernateQuestionsDaoImplementation extends GenericHibernateDao<Questions, Integer> implements QuestionsDao {

	public HibernateQuestionsDaoImplementation() {
		super(Questions.class);
	}

	@Override
	public String getPrimaryKey() {
		return "quesId";
	}
}
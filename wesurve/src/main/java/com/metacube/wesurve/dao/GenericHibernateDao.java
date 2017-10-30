package com.metacube.wesurve.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericHibernateDao<T, ID extends Serializable> implements AbstractDao<T, ID> {
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> modelClass;

	public GenericHibernateDao(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class<T> getModelClass() {
		return modelClass;
	}

	@Override
	public Iterable<T> findAll() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(getModelClass());
		@SuppressWarnings("unchecked")
		List<T> personsList = cr.list();
		return personsList;
	}

	@Override
	public T findOne(final ID primaryKey) {
		return null;
	}

	@Override
	public <S extends T> S save(final S entity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	@Override
	public boolean exists(final ID primaryKey) {
		return false;
	}

	@Override
	public void delete(final T entity) {

	}

	@Override
	public Long count() {
		return null;
	}
}
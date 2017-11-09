package com.metacube.wesurve.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericHibernateDao<T, ID extends Serializable> implements AbstractDao<T, ID> {
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> modelClass;

	public abstract String getPrimaryKey();
	
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
		Criteria criteria = session.createCriteria(getModelClass()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findOne(final ID primaryKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(getModelClass());
		return (T) criteria.add(Restrictions.eq(getPrimaryKey(), primaryKey)).uniqueResult();
	}


	@SuppressWarnings("unchecked")
	@Override
	public <S extends T> S save(final S entity) {
		Session session = this.sessionFactory.getCurrentSession();
		ID id = (ID) session.save(entity);
		return (S) findOne(id);
	}

	@Override
	public boolean exists(final ID primaryKey) {
		boolean result = false;
		if(findOne(primaryKey) != null) {
			result = true;
		}
		
		return result;
	}

	@Override
	public void delete(final T entity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	@Override
	public Long count() {
		return (long) 0;
	}
	
	@Override
	public void update(T entity) {
		Session session = getSessionFactory().getCurrentSession();	
		session.update(entity);
	}
}
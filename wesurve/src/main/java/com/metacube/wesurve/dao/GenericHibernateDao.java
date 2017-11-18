/**
 * The GenericHibernateDao is an abstract class and implements AbstarctDao interface.
 * It defines all common methods related to the tables.
 */
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

	/**
	 * This method return all the records found in the table.
	 */
	@Override
	public Iterable<T> findAll() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(getModelClass()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();
		return list;
	}

	/**
	 * This method returns record from the table which matches to the primary key.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findOne(final ID primaryKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(getModelClass());
		return (T) criteria.add(Restrictions.eq(getPrimaryKey(), primaryKey)).uniqueResult();
	}

	/**
	 * This method saves the new record in the table.
	 */
	@Override
	public <S extends T> S save(final S entity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	/**
	 * This method checks if ID exists in the table or not.
	 */
	@Override
	public boolean exists(final ID primaryKey) {
		boolean result = false;
		if (findOne(primaryKey) != null) {
			result = true;
		}

		return result;
	}

	/**
	 * This method deletes an entity from the table.
	 */
	@Override
	public void delete(final T entity) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	/**
	 * This method updates an entity in the table.
	 */
	@Override
	public void update(T entity) {
		Session session = getSessionFactory().getCurrentSession();
		session.update(entity);
	}
}
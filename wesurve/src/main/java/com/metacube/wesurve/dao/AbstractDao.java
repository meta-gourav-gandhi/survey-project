/**
 * The AbstractDao interface contains methods for CRUD operations related to tables.
 */
package com.metacube.wesurve.dao;

import java.io.Serializable;

public interface AbstractDao<T, ID extends Serializable> {
	<S extends T> S save (S entity);
	T findOne(ID primaryKey);
	Iterable<T> findAll();
	void delete(T entity);
	boolean exists(ID primaryKey);
	void update(T entity);
}
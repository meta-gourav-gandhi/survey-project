/**
 * The HibernateUserRoleDaoImplementation class is a DAO class for UserRole Model.
 * It extends GenericHibernateDao class and implements UserRoleDao interface.
 */
package com.metacube.wesurve.dao.userrole;

import org.springframework.stereotype.Repository;

import com.metacube.wesurve.dao.GenericHibernateDao;
import com.metacube.wesurve.model.UserRole;

@Repository("hibernateUserRoleDaoImplementation")
public class HibernateUserRoleDaoImplementation extends GenericHibernateDao<UserRole, Integer> implements UserRoleDao {

	public HibernateUserRoleDaoImplementation() {
		super(UserRole.class);
	}

	@Override
	public String getPrimaryKey() {
		return "roleId";
	}
}
package com.dps.dao;

import com.dps.domain.entity.Configurations;

/**
 * DAO interface to access {@link Configurations} objects.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public interface ConfigurationsDao extends BaseDao<Configurations>
{
	/**
	 * Returns the configuration object.
	 * @return the configurations object.
	 */
	Configurations getConfigurations();

}

package com.dps.dao.impl;

import java.util.List;

import com.dps.dao.ConfigurationsDao;
import com.dps.domain.entity.Configurations;

/**
 * Default implementation of the {@link ConfigurationsDao} interface.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public class ConfigurationsDaoImpl extends BaseDaoImpl<Configurations> implements ConfigurationsDao
{

	/* (non-Javadoc)
	 * @see com.dps.dao.ConfigurationsDao#getConfigurations()
	 */
	@Override
	public Configurations getConfigurations()
	{
		List<Configurations> configList = findAll(findAll());
		if(configList != null && configList.size() > 0)
		{
			return configList.get(0);
		}
		return null;
	}

}

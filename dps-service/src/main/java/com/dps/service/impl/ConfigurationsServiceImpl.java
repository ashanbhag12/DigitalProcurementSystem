package com.dps.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.ConfigurationsDao;
import com.dps.domain.entity.Configurations;
import com.dps.service.ConfigurationsService;

/**
 * Default implementation of {@link ConfigurationsService} interface.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public class ConfigurationsServiceImpl extends BaseServiceImpl<Configurations> implements ConfigurationsService
{
	@Autowired
	private ConfigurationsDao configurationsDao;

	@PostConstruct
	@Override
	protected void init()
	{
		super.setDao(configurationsDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(0);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.ConfigurationsService#getConfigurations()
	 */
	@Override
	public Configurations getConfigurations()
	{
		return configurationsDao.getConfigurations();
	}

}

package com.dps.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.SupplierOrderDetailsDao;
import com.dps.domain.entity.SupplierOrderDetails;
import com.dps.service.SupplierOrderDetailsService;

/**
 * Default implementation of {@link SupplierOrderDetailsService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class SupplierOrderDetailsServiceImpl extends BaseServiceImpl<SupplierOrderDetails> implements SupplierOrderDetailsService
{
	@Autowired
	private SupplierOrderDetailsDao supplierOrderDetailsDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(supplierOrderDetailsDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<Long, SupplierOrderDetails>(1);
	}

}

package com.dps.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.SupplierOrderDao;
import com.dps.domain.entity.SupplierOrder;
import com.dps.service.SupplierOrderService;

/**
 * Default implementation of {@link SupplierOrderService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class SupplierOrderServiceImpl extends BaseServiceImpl<SupplierOrder> implements SupplierOrderService
{
	@Autowired
	private SupplierOrderDao supplierOrderDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(supplierOrderDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<Long, SupplierOrder>(1);
	}

}

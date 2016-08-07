package com.dps.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.SupplierProductInfoDao;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.SupplierProductInfoService;

/**
 * Default implementation of the {@link SupplierProductInfoService} interface.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public class SupplierProductInfoServiceImpl extends BaseServiceImpl<SupplierProductInfo> implements SupplierProductInfoService
{
	@Autowired
	private SupplierProductInfoDao supplierProductInfoDao;

	@PostConstruct
	@Override
	protected void init()
	{
		super.setDao(supplierProductInfoDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(50);
	}

}

package com.dps.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.SupplierDao;
import com.dps.domain.entity.Supplier;
import com.dps.service.SupplierService;

/**
 * Default implementation of {@link SupplierService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class SupplierServiceImpl extends BaseServiceImpl<Supplier> implements SupplierService
{
	@Autowired
	private SupplierDao supplierDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(supplierDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(5);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.SupplierService#findByInitialsAndName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Supplier> findByInitialsAndName(String initials, String name)
	{
		return supplierDao.findByInitialsAndName(initials, name);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.SupplierService#getAllSupplierInitials()
	 */
	@Override
	public List<String> getAllSupplierInitials()
	{
		return supplierDao.getAllSupplierInitials();
	}

}

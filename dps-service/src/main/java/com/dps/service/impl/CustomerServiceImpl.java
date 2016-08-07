package com.dps.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.CustomerDao;
import com.dps.domain.entity.Customer;
import com.dps.service.CustomerService;

/**
 * Default implementation of {@link CustomerService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService
{
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(customerDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(50);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerService#findByShipmarkAndName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Customer> findByShipmarkAndName(String shipmark, String name)
	{
		return customerDao.findByShipmarkAndName(shipmark, name);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerService#getAllCustomerShipmarks()
	 */
	@Override
	public List<String> getAllCustomerShipmarks()
	{
		return customerDao.getAllCustomerShipmarks();
	}

}

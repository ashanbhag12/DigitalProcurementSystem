package com.dps.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.CustomerOrderDao;
import com.dps.domain.entity.CustomerOrder;
import com.dps.service.CustomerOrderService;

/**
 * Default implementation of {@link CustomerOrderService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerOrderServiceImpl extends BaseServiceImpl<CustomerOrder> implements CustomerOrderService
{
	@Autowired
	private CustomerOrderDao customerOrderDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(customerOrderDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(10);
	}

	@Override
	public List<CustomerOrder> getCustomerOrders(String shipmark, Date startDate, Date endDate)
	{
		return customerOrderDao.getCustomerOrders(shipmark, startDate, endDate);
	}

}

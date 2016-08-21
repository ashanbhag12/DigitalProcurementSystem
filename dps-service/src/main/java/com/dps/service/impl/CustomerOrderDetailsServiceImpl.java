package com.dps.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.CustomerOrderDetailsDao;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.service.CustomerOrderDetailsService;

/**
 * Default implementation of {@link CustomerOrderDetailsService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerOrderDetailsServiceImpl extends BaseServiceImpl<CustomerOrderDetails> implements CustomerOrderDetailsService
{
	@Autowired
	private CustomerOrderDetailsDao customerOrderDetailsDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(customerOrderDetailsDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(1);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerOrderDetailsService#getUnorderedQuantityForProducts(java.util.List)
	 */
	@Override
	public Map<Long, Integer> getUnorderedQuantityForProducts(List<Long> productId)
	{
		return customerOrderDetailsDao.getUnorderedQuantityForProducts(productId);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerOrderDetailsService#getUnorderedProducts()
	 */
	@Override
	public List<CustomerOrderDetails> getUnorderedProducts()
	{
		return customerOrderDetailsDao.getUnorderedProducts();
	}

}

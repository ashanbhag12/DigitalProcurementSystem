package com.dps.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.CustomerProductPreferenceDao;
import com.dps.domain.entity.CustomerProductPreference;
import com.dps.service.CustomerProductPreferenceService;

/**
 * Default implementation of {@link CustomerProductPreferenceService} interface.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public class CustomerProductPreferenceServiceImpl extends BaseServiceImpl<CustomerProductPreference> implements CustomerProductPreferenceService
{
	@Autowired
	private CustomerProductPreferenceDao customerProductPreferenceDao;

	@PostConstruct
	@Override
	protected void init()
	{
		super.setDao(customerProductPreferenceDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(10);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerProductPreferenceService#findPreferencesForCustomer(java.lang.Long)
	 */
	@Override
	public Map<Long, BigDecimal> findPreferencesForCustomer(Long id)
	{
		return customerProductPreferenceDao.findPreferenceForCustomer(id);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.CustomerProductPreferenceService#findAllPreferencesForCustomer(java.lang.Long)
	 */
	@Override
	public List<CustomerProductPreference> findAllPreferencesForCustomer(Long id)
	{
		return customerProductPreferenceDao.findAllPreferencesForCustomer(id);
	}

	@Override
	public Map<Long, BigDecimal> findAllCustomerPreferencesForProduct(Long productId)
	{
		return customerProductPreferenceDao.findAllCustomerPreferencesForProduct(productId);
	}

}

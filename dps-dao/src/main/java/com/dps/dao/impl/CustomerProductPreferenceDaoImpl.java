package com.dps.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dps.commons.domain.JpaEntityId;
import com.dps.dao.CustomerProductPreferenceDao;
import com.dps.domain.entity.CustomerProductPreference;

/**
 * Default implementation of the {@link CustomerProductPreferenceDao} interface.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
public class CustomerProductPreferenceDaoImpl extends BaseDaoImpl<CustomerProductPreference> implements CustomerProductPreferenceDao
{

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerProductPreferenceDao#findPreferenceForCustomer(java.lang.Long)
	 */
	@Override
	public Map<Long, BigDecimal> findPreferenceForCustomer(Long id)
	{
		Map<String, Object> parameters = new HashMap<>();
		Map<Long, BigDecimal> custProdPrefs = new HashMap<>();
		List<JpaEntityId> idList = findAllByNamedQuery(CustomerProductPreference.GET_PREFERENCES_FOR_CUSTOMER, parameters);
		List<CustomerProductPreference> custProdPrefList =  findAll(idList);
		
		for(CustomerProductPreference pref : custProdPrefList)
		{
			custProdPrefs.put(pref.getProduct().getId(), pref.getDiscount());
		}
		
		return custProdPrefs;
	}

}

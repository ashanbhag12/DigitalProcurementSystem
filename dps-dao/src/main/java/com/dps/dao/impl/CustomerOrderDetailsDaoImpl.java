package com.dps.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import com.dps.dao.CustomerOrderDetailsDao;
import com.dps.domain.entity.CustomerOrderDetails;

/**
 * Default implementation of {@link CustomerOrderDetailsDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerOrderDetailsDaoImpl extends BaseDaoImpl<CustomerOrderDetails> implements CustomerOrderDetailsDao
{

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerOrderDetailsDao#getUnorderedQuantityForProducts(java.util.List)
	 */
	@Override
	public Map<Long, Integer> getUnorderedQuantityForProducts(List<Long> productId)
	{
		TypedQuery<Object[]> query = entityManager.createNamedQuery(CustomerOrderDetails.GET_UNORDERED_PRODUCT_COUNT, Object[].class);
		query.setParameter("idList", productId);
		List<Object[]> resultList = query.getResultList();
		
		Map<Long, Integer> results = new HashMap<>();
		for(Object[] result : resultList)
		{
			results.put((Long)result[0], (Integer)result[1]);
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerOrderDetailsDao#getUnorderedProducts()
	 */
	@Override
	public List<CustomerOrderDetails> getUnorderedProducts()
	{
		TypedQuery<CustomerOrderDetails> query = entityManager.createNamedQuery(CustomerOrderDetails.GET_UNORDERED_PRODUCT_DETAILS, CustomerOrderDetails.class);
		return query.getResultList();
	}

}

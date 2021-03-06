package com.dps.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dps.commons.domain.JpaEntityId;
import com.dps.dao.CustomerDao;
import com.dps.domain.entity.Customer;

/**
 * Default implementation of {@link CustomerDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerDaoImpl extends BaseDaoImpl<Customer> implements CustomerDao
{
	private static final List<Customer> EMPTY_CUSTOMER_LIST = new ArrayList<>();

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerDao#findByShipmarkAndName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Customer> findByShipmarkAndName(String shipmark, String name)
	{
		if(StringUtils.isEmpty(name) && StringUtils.isEmpty(shipmark))
		{
			return findAll(findAll());
		}
		
		List<JpaEntityId> customerInitialsId = null, customerNameId = null;
		
		if(StringUtils.isNotBlank(name))
		{
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("name", name + "%");
			customerNameId = findAllByNamedQuery(Customer.FIND_CUSTOMER_BY_NAME, parameters);
		}
		
		if(StringUtils.isNotBlank(shipmark))
		{
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("shipmark", shipmark);
			customerNameId = findAllByNamedQuery(Customer.FIND_CUSTOMER_BY_SHIPMARK, parameters);
		}
		
		if(CollectionUtils.isEmpty(customerInitialsId) && CollectionUtils.isEmpty(customerNameId))
		{
			return EMPTY_CUSTOMER_LIST;
		}
		else if(CollectionUtils.isEmpty(customerNameId))
		{
			return findAll(customerInitialsId);
		}
		else if(CollectionUtils.isEmpty(customerInitialsId))
		{
			return findAll(customerNameId);
		}
		else
		{
			@SuppressWarnings("unchecked")
			Collection<JpaEntityId> result = CollectionUtils.intersection(customerInitialsId, customerNameId);
			return findAll(result);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAll()
	{
		TypedQuery<Long> typedQuery = entityManager.createNamedQuery(Customer.GET_ALL_CUSTOMERS, Long.class);
		
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerDao#getAllCustomerShipmarks()
	 */
	@Override
	public List<String> getAllCustomerShipmarks()
	{
		TypedQuery<String> query = entityManager.createNamedQuery(Customer.GET_ALL_CUSTOMER_SHIPMARK, String.class);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.dps.dao.CustomerDao#getCustomerCount()
	 */
	@Override
	public int getCustomerCount()
	{
		TypedQuery<Long> query = entityManager.createNamedQuery(Customer.GET_CUSTOMER_COUNT, Long.class);
		return query.getSingleResult().intValue();
	}

	@Override
	@Transactional
	public void remove(JpaEntityId id)
	{
		Customer p = find(id);
		p.setActive(false);
	}
}

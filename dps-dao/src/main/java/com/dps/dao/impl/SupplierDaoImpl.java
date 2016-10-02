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
import com.dps.dao.SupplierDao;
import com.dps.domain.entity.Supplier;

/**
 * Default implementation of {@link SupplierDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class SupplierDaoImpl extends BaseDaoImpl<Supplier> implements SupplierDao
{
	public static final List<Supplier> EMPTY_SUPPLIER_LIST = new ArrayList<>();
	
	/* (non-Javadoc)
	 * @see com.dps.dao.SupplierDao#findByInitialsAndName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Supplier> findByInitialsAndName(String initials, String name)
	{
		if(StringUtils.isBlank(initials) && StringUtils.isBlank(name))
		{
			return findAll(findAll());
		}
		
		List<JpaEntityId> suppliersInitialsId = null, suppliersNameId = null;
		
		if(StringUtils.isNotBlank(initials))
		{
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("initials", initials);
			suppliersInitialsId = findAllByNamedQuery(Supplier.FIND_SUPPLIER_BY_INITIALS, parameters);
		}
		if(StringUtils.isNotBlank(name))
		{
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("name", name + "%");
			suppliersInitialsId = findAllByNamedQuery(Supplier.FIND_SUPPLIER_BY_NAME, parameters);
		}
		
		if(CollectionUtils.isEmpty(suppliersInitialsId) && CollectionUtils.isEmpty(suppliersNameId))
		{
			return EMPTY_SUPPLIER_LIST;
		}
		else if(CollectionUtils.isEmpty(suppliersNameId))
		{
			return findAll(suppliersInitialsId);
		}
		else if(CollectionUtils.isEmpty(suppliersInitialsId))
		{
			return findAll(suppliersNameId);
		}
		else
		{
			@SuppressWarnings("unchecked")
			Collection<JpaEntityId> result = CollectionUtils.intersection(suppliersInitialsId, suppliersNameId);
			return findAll(result);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAll()
	{
		TypedQuery<Long> typedQuery = entityManager.createNamedQuery(Supplier.GET_ALL_SUPPLIERS, Long.class);
		
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}
	
	/* (non-Javadoc)
	 * @see com.dps.dao.SupplierDao#getAllSupplierInitials()
	 */
	@Override
	public List<String> getAllSupplierInitials()
	{
		TypedQuery<String> query = entityManager.createNamedQuery(Supplier.GET_ALL_SUPPLIER_INITIALS, String.class);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.dps.dao.SupplierDao#getSupplierCount()
	 */
	@Override
	public int getSupplierCount()
	{
		TypedQuery<Long> query = entityManager.createNamedQuery(Supplier.GET_SUPPLIER_COUNT, Long.class);
		return query.getSingleResult().intValue();
	}
	
	@Override
	@Transactional
	public void remove(JpaEntityId id)
	{
		Supplier s = find(id);
		s.setActive(false);
	}
}

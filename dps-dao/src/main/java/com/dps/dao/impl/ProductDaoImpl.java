package com.dps.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dps.commons.domain.JpaEntityId;
import com.dps.dao.ProductDao;
import com.dps.domain.entity.Product;

/**
 * Default implementation of {@link ProductDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class ProductDaoImpl extends BaseDaoImpl<Product> implements ProductDao
{
	private static final List<Product> EMPTY_PRODUCT_LIST = new ArrayList<>();

	@Override
	public List<Product> findByCode(String code)
	{
		List<JpaEntityId> productCodeId = null;
		
		if(StringUtils.isBlank(code))
		{
			return findAll(findAll());
		}
		
		if(StringUtils.isNotBlank(code))
		{
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("code", code + "%");
			productCodeId = findAllByNamedQuery(Product.FIND_PRODUCT_BY_CODE, parameters);
		}
		
		if(CollectionUtils.isEmpty(productCodeId))
		{
			return EMPTY_PRODUCT_LIST;
		}
		
		return findAll(productCodeId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAll()
	{
		TypedQuery<Long> typedQuery = entityManager.createNamedQuery(Product.GET_ALL_PRODUCTS, Long.class);
		
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}
	
	@Override
	@Transactional
	public void remove(JpaEntityId id)
	{
		Product p = find(id);
		p.setActive(false);
	}

	@Override
	public List<String> getAllProductCodes()
	{
		TypedQuery<String> query = entityManager.createNamedQuery(Product.GET_ALL_PRODUCT_CODE, String.class);
		return query.getResultList();
	}

	@Override
	public int getProductCount() 
	{
		TypedQuery<Long> query = entityManager.createNamedQuery(Product.GET_PRODUCT_COUNT, Long.class);
		return query.getSingleResult().intValue();
	}

}

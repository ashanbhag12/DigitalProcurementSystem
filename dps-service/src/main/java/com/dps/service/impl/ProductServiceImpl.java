package com.dps.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.ProductDao;
import com.dps.domain.entity.Product;
import com.dps.service.ProductService;

/**
 * Default implementation of {@link ProductService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService
{
	@Autowired
	private ProductDao productDao;

	@Override
	@PostConstruct
	protected void init()
	{
		super.setDao(productDao);
		initializeCache();
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(100);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.ProductService#findByCode(java.lang.String)
	 */
	@Override
	public List<Product> findByCode(String code)
	{
		return productDao.findByCode(code);
	}

	/* (non-Javadoc)
	 * @see com.dps.service.ProductService#getAllProductCodes()
	 */
	@Override
	public List<String> getAllProductCodes()
	{
		return productDao.getAllProductCodes();
	}

	@Override
	public int getProductCount() 
	{
		return productDao.getProductCount();
	}

}
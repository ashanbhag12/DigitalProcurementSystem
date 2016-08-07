package com.dps.dao;

import java.util.List;

import com.dps.domain.entity.Product;

/**
 * DAO interface to access {@link Product} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface ProductDao extends BaseDao<Product>
{

	/**
	 * Returns {@link List} of {@link Product} matching the specified code.
	 * @param code - The product code. Will be searched as code%.
	 * @return {@link List} of {@link Product} matching the specified code.
	 */
	List<Product> findByCode(String code);

	/**
	 * Returns {@link List} of codes of all the product present in the system.
	 * @return {@link List} of codes of all the product present in the system.
	 */
	List<String> getAllProductCodes();

}

package com.dps.service;

import java.util.List;

import com.dps.domain.entity.Product;

/**
 * Service interface to access {@link Product} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface ProductService extends BaseService<Product>
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

	/**
	 * Returns the count of all the products that are stored in the system.
	 * @return the count of all the products that are stored in the system.
	 */
	int getProductCount();
}

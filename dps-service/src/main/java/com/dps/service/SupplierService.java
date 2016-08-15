package com.dps.service;

import java.util.List;

import com.dps.domain.entity.Supplier;

/**
 * Service interface to access {@link Supplier} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface SupplierService extends BaseService<Supplier>
{

	/**
	 * Returns a {@link List} of {@link Supplier} matching the name and initials.
	 * @param initials - Initials of the supplier to be fetched. Can be null. 
	 * @param name - The name of the supplier to be fetched. The search will be performed as name%. Can be null.
	 * @return - {@link List} of {@link Supplier} matching the name and initials.
	 */
	List<Supplier> findByInitialsAndName(String initials, String name);

	/**
	 * Returns {@link List} of {@link String} containing the initials of all the suppliers in the system.
	 * @return - {@link List} of {@link String} containing the initials of all the suppliers in the system.
	 */
	List<String> getAllSupplierInitials();

	/**
	 * Returns the count of the number of suppliers in the system.
	 * @return the count of the number of suppliers in the system.
	 */
	int getSupplierCount();
}

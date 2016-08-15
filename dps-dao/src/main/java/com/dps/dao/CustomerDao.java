package com.dps.dao;

import java.util.List;

import com.dps.domain.entity.Customer;

/**
 * DAO interface to access {@link Customer} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface CustomerDao extends BaseDao<Customer>
{

	/**
	 * Finds and returns a {@link List} of {@link Customer} matching the shipmark and / or name. 
	 * @param shipmark - Shipping mark of the customer.
	 * @param name - Name of the customer. Search will be performed as name%
	 * @return - {@link List} of {@link Customer} matching the shipmark and / or name. 
	 */
	List<Customer> findByShipmarkAndName(String shipmark, String name);

	/**
	 * Returns {@link List} Shipping marks of all the customers present in the system. 
	 * @return - {@link List} Shipping marks of all the customers present in the system.
	 */
	List<String> getAllCustomerShipmarks();

	/**
	 * Returns the count of total number of customers present in the system.
	 * @return the count of total number of customers present in the system.
	 */
	int getCustomerCount();

}

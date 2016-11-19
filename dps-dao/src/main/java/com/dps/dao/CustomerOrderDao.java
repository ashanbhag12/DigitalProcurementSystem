package com.dps.dao;

import java.util.Date;
import java.util.List;

import com.dps.domain.entity.CustomerOrder;

/**
 * DAO interface to access {@link CustomerOrder} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface CustomerOrderDao extends BaseDao<CustomerOrder>
{
	/**
	 * Returns {@link List} of all the orders placed by the customer specified between the start and end dates both inclusive.
	 * @param shipmark - The shipping mark of the customer whose orders are to be placed.
	 * @param startDate - The start date from which the orders are to be fetched.
	 * @param endDate - The end date till which the order placed will be fetched.
	 * @return Returns {@link List} of all the orders placed by the customer specified between the start and end dates both inclusive.
	 */
	List<CustomerOrder> getCustomerOrders(String shipmark, Date startDate, Date endDate);

}

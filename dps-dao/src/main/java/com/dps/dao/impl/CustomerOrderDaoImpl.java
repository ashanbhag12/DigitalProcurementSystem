package com.dps.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.dps.dao.CustomerOrderDao;
import com.dps.domain.entity.CustomerOrder;

/**
 * Default implementation of {@link CustomerOrderDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CustomerOrderDaoImpl extends BaseDaoImpl<CustomerOrder> implements CustomerOrderDao
{

	@Override
	public List<CustomerOrder> getCustomerOrders(String shipmark, Date startDate, Date endDate)
	{
		endDate = DateUtils.addDays(endDate, 1);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("startDate", startDate);
		parameters.put("endDate", endDate);
		parameters.put("shipmark", shipmark);
		
		return findAll(findAllByNamedQuery(CustomerOrder.GET_CUSTOMER_ORDERS, parameters));
	}

}

package com.dps.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.dps.dao.SupplierOrderDao;
import com.dps.domain.entity.SupplierOrder;

/**
 * Default implementation of {@link SupplierOrderDao} interface
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class SupplierOrderDaoImpl extends BaseDaoImpl<SupplierOrder> implements SupplierOrderDao
{

	@Override
	public List<SupplierOrder> getUncompletedOrders()
	{
		Map<String, ?> parameters = new HashMap<>();
		return findAll(findAllByNamedQuery(SupplierOrder.GET_UNCOMPLETED_SUPPLIER_ORDERS, parameters));
	}

	@Override
	public List<SupplierOrder> getSupplierOrders(String supplierInitials, Date startDate, Date endDate)
	{
		endDate = DateUtils.addDays(endDate, 1);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("startDate", startDate);
		parameters.put("endDate", endDate);
		
		if(StringUtils.equalsIgnoreCase("ALL", supplierInitials))
		{
			return findAll(findAllByNamedQuery(SupplierOrder.GET_ALL_SUPPLIER_ORDERS_FOR_DATE_RANGE, parameters));
		}
		
		parameters.put("supplierInitials", supplierInitials);
		return findAll(findAllByNamedQuery(SupplierOrder.GET_SUPPLIER_ORDERS_FOR_DATE_RANGE, parameters));
	}

}

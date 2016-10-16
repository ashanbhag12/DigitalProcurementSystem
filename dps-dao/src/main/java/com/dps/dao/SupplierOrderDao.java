package com.dps.dao;

import java.util.Date;
import java.util.List;

import com.dps.domain.entity.SupplierOrder;


/**
 * DAO interface to access {@link SupplierOrder} objects.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface SupplierOrderDao extends BaseDao<SupplierOrder>
{
	/**
	 * Returns a {@link List} of suppliers orders that are either in partially completed or not started state.
	 * @return {@link List} of {@link SupplierOrder} that are either in partially completed or not started state.
	 */
	List<SupplierOrder> getUncompletedOrders();
	
	/**
	 * Returns a {@link List} of {@link SupplierOrder} that have been placed to the specified supplier between the specified start and end dates.
	 * 
	 * @param supplierInitials - The supplier initials.
	 * @param startDate - Start Date
	 * @param endDate - End date
	 * @return - {@link List} of {@link SupplierOrder} that have been placed to the specified supplier between the specified start and end dates.
	 */
	List<SupplierOrder> getSupplierOrders(String supplierInitials, Date startDate, Date endDate);
}

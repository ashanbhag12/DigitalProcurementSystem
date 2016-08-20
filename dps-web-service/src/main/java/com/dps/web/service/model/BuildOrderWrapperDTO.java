package com.dps.web.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps the customer details and the order details
 *
 * @see
 *
 * @Date 20-Aug-2016
 *
 * @author akshay
 */
public class BuildOrderWrapperDTO
{
	private String customerShipmark;
	private Date orderDate;
	private List<BuildOrderDTO> orderItems = new ArrayList<>();

	public String getCustomerShipmark()
	{
		return customerShipmark;
	}

	public void setCustomerShipmark(String customerShipmark)
	{
		this.customerShipmark = customerShipmark;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public List<BuildOrderDTO> getOrderItems()
	{
		return orderItems;
	}

	public void setOrderItems(List<BuildOrderDTO> orderItems)
	{
		this.orderItems = orderItems;
	}

}

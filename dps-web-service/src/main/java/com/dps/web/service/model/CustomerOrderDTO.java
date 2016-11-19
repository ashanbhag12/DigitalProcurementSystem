package com.dps.web.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps the customer orders.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
public class CustomerOrderDTO
{
	private Long id;
	private String shipmark;
	private Date orderDate;
	private Date orderCompletedDate;
	private String status;
	private List<CustomerOrderDetailsDTO> lineItems = new ArrayList<>();
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getShipmark()
	{
		return shipmark;
	}
	public void setShipmark(String shipmark)
	{
		this.shipmark = shipmark;
	}
	public Date getOrderDate()
	{
		return orderDate;
	}
	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}
	public Date getOrderCompletedDate()
	{
		return orderCompletedDate;
	}
	public void setOrderCompletedDate(Date orderCompletedDate)
	{
		this.orderCompletedDate = orderCompletedDate;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public List<CustomerOrderDetailsDTO> getLineItems()
	{
		return lineItems;
	}
	public void setLineItems(List<CustomerOrderDetailsDTO> lineItems)
	{
		this.lineItems = lineItems;
	}
}

package com.dps.web.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps the field that will be displayed on the view supplier order screen
 *
 * @see
 *
 * @Date Oct 16, 2016
 *
 * @author akshay
 */
public class ViewSupplierOrderDTO
{
	private String supplierInitials;
	private Date orderDate;
	private String Status;
	private long id;
	private List<ViewSupplierOrderDetailsDTO> details = new ArrayList<>();
	public String getSupplierInitials()
	{
		return supplierInitials;
	}
	public void setSupplierInitials(String supplierInitials)
	{
		this.supplierInitials = supplierInitials;
	}
	public Date getOrderDate()
	{
		return orderDate;
	}
	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}
	public String getStatus()
	{
		return Status;
	}
	public void setStatus(String status)
	{
		Status = status;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public List<ViewSupplierOrderDetailsDTO> getDetails()
	{
		return details;
	}
	public void setDetails(List<ViewSupplierOrderDetailsDTO> details)
	{
		this.details = details;
	}
	
}

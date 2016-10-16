package com.dps.web.service.model;

/**
 * Maps the field that will be displayed on the update order details section
 *
 * @see
 *
 * @Date Oct 16, 2016
 *
 * @author akshay
 */
public class SupplierOrderDetailsDTO
{
	private String productCode;
	private String customerInitials;
	private String remarks;
	private int orderedQuantity;
	private int prevoiuslyReceivedQuantity;
	private Integer receivedQuantity;
	private int pendingQuantity;
	private long id;
	
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getCustomerInitials()
	{
		return customerInitials;
	}
	public void setCustomerInitials(String customerInitials)
	{
		this.customerInitials = customerInitials;
	}
	public String getRemarks()
	{
		return remarks;
	}
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	public int getOrderedQuantity()
	{
		return orderedQuantity;
	}
	public void setOrderedQuantity(int orderedQuantity)
	{
		this.orderedQuantity = orderedQuantity;
	}
	public int getPrevoiuslyReceivedQuantity()
	{
		return prevoiuslyReceivedQuantity;
	}
	public void setPrevoiuslyReceivedQuantity(int prevoiuslyReceivedQuantity)
	{
		this.prevoiuslyReceivedQuantity = prevoiuslyReceivedQuantity;
	}
	public Integer getReceivedQuantity()
	{
		return receivedQuantity;
	}
	public void setReceivedQuantity(Integer receivedQuantity)
	{
		this.receivedQuantity = receivedQuantity;
	}
	public int getPendingQuantity()
	{
		return pendingQuantity;
	}
	public void setPendingQuantity(int pendingQuantity)
	{
		this.pendingQuantity = pendingQuantity;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
}

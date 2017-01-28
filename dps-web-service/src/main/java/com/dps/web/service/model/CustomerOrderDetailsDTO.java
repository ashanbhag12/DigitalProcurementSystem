package com.dps.web.service.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Maps the customer order line items.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
public class CustomerOrderDetailsDTO
{
	private String productCode;
	private String supplierInitials;
	private Integer quantity;
	private String status;
	private Date orderPlacedDate;
	private Date orderReceivedDate;
	private Integer receivedQuantity;
	private Integer lastReceivedQuantity;
	private String remarks;
	private BigDecimal productPrice;
	private boolean isSelected = false;
	private Long id;
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getSupplierInitials()
	{
		return supplierInitials;
	}
	public void setSupplierInitials(String supplierInitials)
	{
		this.supplierInitials = supplierInitials;
	}
	public Integer getQuantity()
	{
		return quantity;
	}
	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Date getOrderPlacedDate()
	{
		return orderPlacedDate;
	}
	public void setOrderPlacedDate(Date orderPlacedDate)
	{
		this.orderPlacedDate = orderPlacedDate;
	}
	public Date getOrderReceivedDate()
	{
		return orderReceivedDate;
	}
	public void setOrderReceivedDate(Date orderReceivedDate)
	{
		this.orderReceivedDate = orderReceivedDate;
	}
	public Integer getReceivedQuantity()
	{
		return receivedQuantity;
	}
	public void setReceivedQuantity(Integer receivedQuantity)
	{
		this.receivedQuantity = receivedQuantity;
	}
	public String getRemarks()
	{
		return remarks;
	}
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	public BigDecimal getProductPrice()
	{
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice)
	{
		this.productPrice = productPrice;
	}
	public boolean isSelected()
	{
		return isSelected;
	}
	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}
	public Integer getLastReceivedQuantity()
	{
		return lastReceivedQuantity;
	}
	public void setLastReceivedQuantity(Integer lastReceivedQuantity)
	{
		this.lastReceivedQuantity = lastReceivedQuantity;
	}
}

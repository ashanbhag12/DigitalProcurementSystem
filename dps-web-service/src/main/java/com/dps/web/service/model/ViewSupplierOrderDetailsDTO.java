package com.dps.web.service.model;

import java.math.BigDecimal;

public class ViewSupplierOrderDetailsDTO
{
	private String supplierProductCode;
	private String productCode;
	private String productDescription;
	private Integer quantity;
	private BigDecimal pricePerItem;
	private String remarks;
	private String customerDetails;
	private Integer packageing;
	private BigDecimal cbm;
	private BigDecimal gw;
	
	public String getSupplierProductCode()
	{
		return supplierProductCode;
	}
	public void setSupplierProductCode(String supplierProductCode)
	{
		this.supplierProductCode = supplierProductCode;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription = productDescription;
	}
	public Integer getQuantity()
	{
		return quantity;
	}
	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
	public BigDecimal getPricePerItem()
	{
		return pricePerItem;
	}
	public void setPricePerItem(BigDecimal pricePerItem)
	{
		this.pricePerItem = pricePerItem;
	}
	public String getRemarks()
	{
		return remarks;
	}
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	public String getCustomerDetails()
	{
		return customerDetails;
	}
	public void setCustomerDetails(String customerDetails)
	{
		this.customerDetails = customerDetails;
	}
	public Integer getPackageing()
	{
		return packageing;
	}
	public void setPackageing(Integer packageing)
	{
		this.packageing = packageing;
	}
	public BigDecimal getCbm()
	{
		return cbm;
	}
	public void setCbm(BigDecimal cbm)
	{
		this.cbm = cbm;
	}
	public BigDecimal getGw()
	{
		return gw;
	}
	public void setGw(BigDecimal gw)
	{
		this.gw = gw;
	}
	
}

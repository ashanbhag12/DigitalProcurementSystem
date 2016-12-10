package com.dps.web.service.model;

import java.math.BigDecimal;

/**
 * Maps to the field that needs to be displayed on the Place order page.
 *
 * @see
 *
 * @Date 21-Aug-2016
 *
 * @author akshay
 */
public class PlaceOrderDTO
{
	private String productCode;
	private Long productId;
	private Integer quantity;
	private String remarks;
	private Integer moq;
	private String supplierInitials;
	private String customerDetails;
	private boolean toOrder;
	private String idList;
	private String supplierProductCode;
	private String productDescription;
	private BigDecimal pricePerItem;
	private Integer packageing;
	private BigDecimal cbm;
	private BigDecimal gw;
	

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public Integer getMoq()
	{
		return moq;
	}

	public void setMoq(Integer moq)
	{
		this.moq = moq;
	}

	public String getSupplierInitials()
	{
		return supplierInitials;
	}

	public void setSupplierInitials(String supplierInitials)
	{
		this.supplierInitials = supplierInitials;
	}

	public String getCustomerDetails()
	{
		return customerDetails;
	}

	public void setCustomerDetails(String customerDetails)
	{
		this.customerDetails = customerDetails;
	}

	public boolean isToOrder()
	{
		return toOrder;
	}

	public void setToOrder(boolean toOrder)
	{
		this.toOrder = toOrder;
	}

	public String getIdList()
	{
		return idList;
	}

	public void setIdList(String idList)
	{
		this.idList = idList;
	}

	public String getSupplierProductCode()
	{
		return supplierProductCode;
	}

	public void setSupplierProductCode(String supplierProductCode)
	{
		this.supplierProductCode = supplierProductCode;
	}

	public String getProductDescription()
	{
		return productDescription;
	}

	public void setProductDescription(String productDescription)
	{
		this.productDescription = productDescription;
	}

	public BigDecimal getPricePerItem()
	{
		return pricePerItem;
	}

	public void setPricePerItem(BigDecimal pricePerItem)
	{
		this.pricePerItem = pricePerItem;
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

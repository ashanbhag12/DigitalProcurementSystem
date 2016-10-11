package com.dps.web.service.model;

/**
 * Stores simple supplier id, product id and supplier defined product code.
 *
 * @see
 *
 * @Date Jul 30, 2016
 *
 * @author akshay
 */
public class SuppProdInfo
{
	private String supplierInitials;
	private String productCode;
	private String supplierProductCode;
	private String supplierPrice;
	
	public String getSupplierInitials()
	{
		return supplierInitials;
	}
	public void setSupplierInitials(String supplierInitials)
	{
		this.supplierInitials = supplierInitials;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getSupplierProductCode()
	{
		return supplierProductCode;
	}
	public void setSupplierProductCode(String supplierProductCode)
	{
		this.supplierProductCode = supplierProductCode;
	}
	public String getSupplierPrice()
	{
		return supplierPrice;
	}
	public void setSupplierPrice(String supplierPrice)
	{
		this.supplierPrice = supplierPrice;
	}
}

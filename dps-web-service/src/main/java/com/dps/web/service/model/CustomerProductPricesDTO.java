package com.dps.web.service.model;

import java.math.BigDecimal;

/**
 * This class maps the customer-product discount and the price of the products
 * for a customer.
 * 
 * @see
 *
 * @Date 15-Aug-2016
 *
 * @author akshay
 */
public class CustomerProductPricesDTO
{
	private String productCode;
	private BigDecimal productPrice; 
	private BigDecimal productMargin;
	private BigDecimal productMarginPercentage;
	private BigDecimal customerProductMargin;
	private BigDecimal customerProductMarginPercentage;
	private BigDecimal cost;
	private String productDescription;
	private Integer cartoonQuantity;
	private BigDecimal grossWeight;
	private BigDecimal cbm;
	private BigDecimal calculatedCost;
	private BigDecimal originalCost;
	private boolean toExport = false;
	private String supplierInitials;

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public BigDecimal getProductPrice()
	{
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice)
	{
		this.productPrice = productPrice;
	}

	public BigDecimal getProductMargin()
	{
		return productMargin;
	}

	public void setProductMargin(BigDecimal productMargin)
	{
		this.productMargin = productMargin;
	}

	public BigDecimal getCost()
	{
		return cost;
	}

	public void setCost(BigDecimal cost)
	{
		this.cost = cost;
	}

	public BigDecimal getCustomerProductMargin()
	{
		return customerProductMargin;
	}

	public void setCustomerProductMargin(BigDecimal customerProductMargin)
	{
		this.customerProductMargin = customerProductMargin;
	}

	public boolean isToExport()
	{
		return toExport;
	}

	public void setToExport(boolean toExport)
	{
		this.toExport = toExport;
	}

	public String getProductDescription()
	{
		return productDescription;
	}

	public void setProductDescription(String productDescription)
	{
		this.productDescription = productDescription;
	}

	public Integer getCartoonQuantity()
	{
		return cartoonQuantity;
	}

	public void setCartoonQuantity(Integer cartoonQuantity)
	{
		this.cartoonQuantity = cartoonQuantity;
	}

	public BigDecimal getGrossWeight()
	{
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight)
	{
		this.grossWeight = grossWeight;
	}

	public BigDecimal getCbm()
	{
		return cbm;
	}

	public void setCbm(BigDecimal cbm)
	{
		this.cbm = cbm;
	}

	public String getSupplierInitials()
	{
		return supplierInitials;
	}

	public void setSupplierInitials(String supplierInitials)
	{
		this.supplierInitials = supplierInitials;
	}

	public BigDecimal getProductMarginPercentage()
	{
		return productMarginPercentage;
	}

	public void setProductMarginPercentage(BigDecimal productMarginPercentage)
	{
		this.productMarginPercentage = productMarginPercentage;
	}

	public BigDecimal getCustomerProductMarginPercentage()
	{
		return customerProductMarginPercentage;
	}

	public void setCustomerProductMarginPercentage(
			BigDecimal customerProductMarginPercentage)
	{
		this.customerProductMarginPercentage = customerProductMarginPercentage;
	}

	public BigDecimal getCalculatedCost()
	{
		return calculatedCost;
	}

	public void setCalculatedCost(BigDecimal calculatedCost)
	{
		this.calculatedCost = calculatedCost;
	}

	public BigDecimal getOriginalCost()
	{
		return originalCost;
	}

	public void setOriginalCost(BigDecimal originalCost)
	{
		this.originalCost = originalCost;
	}
}

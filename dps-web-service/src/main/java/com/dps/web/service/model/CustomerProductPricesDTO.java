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
	private BigDecimal customerMargin;
	private BigDecimal productMargin;
	private BigDecimal customerProductMargin;
	private BigDecimal cost;

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

	public BigDecimal getCustomerMargin()
	{
		return customerMargin;
	}

	public void setCustomerMargin(BigDecimal customerMargin)
	{
		this.customerMargin = customerMargin;
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

}

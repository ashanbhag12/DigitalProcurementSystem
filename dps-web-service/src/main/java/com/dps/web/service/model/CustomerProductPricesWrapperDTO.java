package com.dps.web.service.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the customer information and {@link List} of
 * {@link CustomerProductPricesDTO} objects.
 *
 * @see
 *
 * @Date 20-Aug-2016
 *
 * @author akshay
 */
public class CustomerProductPricesWrapperDTO
{
	private String shipmark;
	private BigDecimal additionalCustomerMargin;
	private BigDecimal additionalCustomerMarginPercentage;
	private List<CustomerProductPricesDTO> customerProductPrices = new ArrayList<>();

	public String getShipmark()
	{
		return shipmark;
	}

	public void setShipmark(String shipmark)
	{
		this.shipmark = shipmark;
	}

	public BigDecimal getAdditionalCustomerMargin()
	{
		return additionalCustomerMargin;
	}

	public void setAdditionalCustomerMargin(BigDecimal additionalCustomerMargin)
	{
		this.additionalCustomerMargin = additionalCustomerMargin;
	}

	public List<CustomerProductPricesDTO> getCustomerProductPrices()
	{
		return customerProductPrices;
	}

	public void setCustomerProductPrices(List<CustomerProductPricesDTO> customerProductPrices)
	{
		this.customerProductPrices = customerProductPrices;
	}

	public BigDecimal getAdditionalCustomerMarginPercentage()
	{
		return additionalCustomerMarginPercentage;
	}

	public void setAdditionalCustomerMarginPercentage(
			BigDecimal additionalCustomerMarginPercentage)
	{
		this.additionalCustomerMarginPercentage = additionalCustomerMarginPercentage;
	}

}

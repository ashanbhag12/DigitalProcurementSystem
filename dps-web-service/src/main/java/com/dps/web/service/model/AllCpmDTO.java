package com.dps.web.service.model;

import java.math.BigDecimal;

/**
 * Maps the all CPM for the customers.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
public class AllCpmDTO
{
	private String shipmark;
	private BigDecimal discount;
	public String getShipmark()
	{
		return shipmark;
	}
	public void setShipmark(String shipmark)
	{
		this.shipmark = shipmark;
	}
	public BigDecimal getDiscount()
	{
		return discount;
	}
	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}
}
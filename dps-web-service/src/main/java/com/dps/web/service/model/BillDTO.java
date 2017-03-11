package com.dps.web.service.model;

import java.math.BigDecimal;

public class BillDTO extends BuildOrderWrapperDTO
{
	private BigDecimal additionalCost;
	private String additionalCostDetails;
	private BigDecimal additionalDiscount;
	private String additionalDiscountDetails;
	
	public BigDecimal getAdditionalCost()
	{
		return additionalCost;
	}
	public void setAdditionalCost(BigDecimal additionalCost)
	{
		this.additionalCost = additionalCost;
	}
	public String getAdditionalCostDetails()
	{
		return additionalCostDetails;
	}
	public void setAdditionalCostDetails(String additionalCostDetails)
	{
		this.additionalCostDetails = additionalCostDetails;
	}
	public BigDecimal getAdditionalDiscount()
	{
		return additionalDiscount;
	}
	public void setAdditionalDiscount(BigDecimal additionalDiscount)
	{
		this.additionalDiscount = additionalDiscount;
	}
	public String getAdditionalDiscountDetails()
	{
		return additionalDiscountDetails;
	}
	public void setAdditionalDiscountDetails(String additionalDiscountDetails)
	{
		this.additionalDiscountDetails = additionalDiscountDetails;
	}
}

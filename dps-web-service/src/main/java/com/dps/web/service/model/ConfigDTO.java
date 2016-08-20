package com.dps.web.service.model;

import java.math.BigDecimal;

/**
 * Single config DTO object that stores the global configurations
 *
 * @see
 *
 * @Date 15-Aug-2016
 *
 * @author akshay
 */
public class ConfigDTO
{
	private BigDecimal exchangeRate;
	private BigDecimal costPerCbm;
	private BigDecimal costPerGrossWeight;
	
	public BigDecimal getExchangeRate()
	{
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate)
	{
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getCostPerCbm()
	{
		return costPerCbm;
	}
	public void setCostPerCbm(BigDecimal costPerCbm)
	{
		this.costPerCbm = costPerCbm;
	}
	public BigDecimal getCostPerGrossWeight()
	{
		return costPerGrossWeight;
	}
	public void setCostPerGrossWeight(BigDecimal costPerGrossWeight)
	{
		this.costPerGrossWeight = costPerGrossWeight;
	}
	
	
}

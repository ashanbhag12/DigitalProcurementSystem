package com.dps.web.service.model;

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
	private double exchangeRate;
	private double costPerCbm;
	private double costPerGrossWeight;
	
	public double getExchangeRate()
	{
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate)
	{
		this.exchangeRate = exchangeRate;
	}
	public double getCostPerCbm()
	{
		return costPerCbm;
	}
	public void setCostPerCbm(double costPerCbm)
	{
		this.costPerCbm = costPerCbm;
	}
	public double getCostPerGrossWeight()
	{
		return costPerGrossWeight;
	}
	public void setCostPerGrossWeight(double costPerGrossWeight)
	{
		this.costPerGrossWeight = costPerGrossWeight;
	}
	
	
}

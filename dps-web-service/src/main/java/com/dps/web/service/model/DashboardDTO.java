package com.dps.web.service.model;

/**
 * The simple dashboard entity that will be sent to the UI form.
 *
 * @see
 *
 * @Date Aug 15, 2016
 *
 * @author akshay
 */
public class DashboardDTO 
{
	private double exchangeRate;
	private int productsCount;
	private int supplierCount;
	private int customerCount;
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public int getProductsCount() {
		return productsCount;
	}
	public void setProductsCount(int productsCount) {
		this.productsCount = productsCount;
	}
	public int getSupplierCount() {
		return supplierCount;
	}
	public void setSupplierCount(int supplierCount) {
		this.supplierCount = supplierCount;
	}
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}
	
	
}

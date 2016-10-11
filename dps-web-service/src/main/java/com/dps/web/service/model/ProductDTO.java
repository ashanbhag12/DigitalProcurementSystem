package com.dps.web.service.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The simple product entity that will be sent from the UI form.
 *
 * @see
 *
 * @Date Jul 30, 2016
 *
 * @author akshay
 */
public class ProductDTO
{
	private Long id;
	private String productCode;
	private Integer cartoonQuantity;
	private BigDecimal cbm;
	private BigDecimal weight;
	private String description;
	private String dummyCode;
	private Integer moq;
	private Boolean isValid;
	private BigDecimal defaultMargin;
	private BigDecimal defaultMarginPercentage;
	private BigDecimal price;
	private BigDecimal cost;
	private List<SuppProdInfo> supplierProductInfoList = new ArrayList<>();
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public Integer getCartoonQuantity()
	{
		return cartoonQuantity;
	}
	public void setCartoonQuantity(Integer cartoonQuantity)
	{
		this.cartoonQuantity = cartoonQuantity;
	}
	public BigDecimal getCbm()
	{
		return cbm;
	}
	public void setCbm(BigDecimal cbm)
	{
		this.cbm = cbm;
	}
	public BigDecimal getWeight()
	{
		return weight;
	}
	public void setWeight(BigDecimal weight)
	{
		this.weight = weight;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getMoq()
	{
		return moq;
	}
	public void setMoq(Integer moq)
	{
		this.moq = moq;
	}
	public Boolean getIsValid()
	{
		return isValid;
	}
	public void setIsValid(Boolean isValid)
	{
		this.isValid = isValid;
	}
	public BigDecimal getDefaultMargin()
	{
		return defaultMargin;
	}
	public void setDefaultMargin(BigDecimal defaultMargin)
	{
		this.defaultMargin = defaultMargin;
	}
	public List<SuppProdInfo> getSupplierProductInfoList()
	{
		return supplierProductInfoList;
	}
	public void setSupplierProductInfoList(List<SuppProdInfo> supplierProductInfoList)
	{
		this.supplierProductInfoList = supplierProductInfoList;
	}
	public BigDecimal getDefaultMarginPercentage()
	{
		return defaultMarginPercentage;
	}
	public void setDefaultMarginPercentage(BigDecimal defaultMarginPercentage)
	{
		this.defaultMarginPercentage = defaultMarginPercentage;
	}
	public BigDecimal getPrice()
	{
		return price;
	}
	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}
	public BigDecimal getCost()
	{
		return cost;
	}
	public void setCost(BigDecimal cost)
	{
		this.cost = cost;
	}
	public String getDummyCode()
	{
		return dummyCode;
	}
	public void setDummyCode(String dummyCode)
	{
		this.dummyCode = dummyCode;
	}
}

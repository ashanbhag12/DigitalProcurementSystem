package com.dps.web.service.model;

import java.math.BigDecimal;


/**
 * Maps the build order table before calculating the cost of the products.
 *
 * @see
 *
 * @Date 20-Aug-2016
 *
 * @author akshay
 */
public class BuildOrderDTO
{
	private String productCode;
	private Long productId;
	private String selectedSupplierInitials;
	private Integer quantity;
	private String remarks;
	private BigDecimal unitCost;
	private BigDecimal custProdDiscount;
	private BigDecimal custProdDiscountPercent;
	private Boolean isMoqSatisfied = Boolean.FALSE;
	private Integer catronQuantity;

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getSelectedSupplierInitials()
	{
		return selectedSupplierInitials;
	}

	public void setSelectedSupplierInitials(String selectedSupplierInitials)
	{
		this.selectedSupplierInitials = selectedSupplierInitials;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public BigDecimal getUnitCost()
	{
		return unitCost;
	}

	public void setUnitCost(BigDecimal unitCost)
	{
		this.unitCost = unitCost;
	}

	public BigDecimal getCustProdDiscount()
	{
		return custProdDiscount;
	}

	public void setCustProdDiscount(BigDecimal custProdDiscount)
	{
		this.custProdDiscount = custProdDiscount;
	}

	public Boolean getIsMoqSatisfied()
	{
		return isMoqSatisfied;
	}

	public void setIsMoqSatisfied(Boolean isMoqSatisfied)
	{
		this.isMoqSatisfied = isMoqSatisfied;
	}

	public BigDecimal getCustProdDiscountPercent()
	{
		return custProdDiscountPercent;
	}

	public void setCustProdDiscountPercent(BigDecimal custProdDiscountPercent)
	{
		this.custProdDiscountPercent = custProdDiscountPercent;
	}

	public Integer getCatronQuantity()
	{
		return catronQuantity;
	}

	public void setCatronQuantity(Integer catronQuantity)
	{
		this.catronQuantity = catronQuantity;
	}
	
}

package com.dps.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class stores the information of the product and supplier mappings.
 *
 * @see
 *
 * @Date Jul 16, 2016
 *
 * @author akshay
 */
@Entity
@Table(name = "DPS_PROD_SUPP_INFO")
public class SupplierProductInfo extends EntityBase
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PROD_ID")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="SUPP_ID")
	private Supplier supplier;
	
	@Basic
	@Column(name="SUPP_PROD_NAME")
	private String supplierProductName;
	
	@Basic
	@Column(name="SUPP_PROD_PRICE")
	private BigDecimal supplierPrice;
	
	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Supplier getSupplier()
	{
		return supplier;
	}

	public void setSupplier(Supplier supplier)
	{
		this.supplier = supplier;
	}

	public String getSupplierProductName()
	{
		return supplierProductName;
	}

	public void setSupplierProductName(String supplierProductName)
	{
		this.supplierProductName = supplierProductName;
	}

	public Long getId()
	{
		return id;
	}

	public BigDecimal getSupplierPrice()
	{
		return supplierPrice;
	}

	public void setSupplierPrice(BigDecimal supplierPrice)
	{
		this.supplierPrice = supplierPrice;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((supplierProductName == null) ? 0 : supplierProductName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupplierProductInfo other = (SupplierProductInfo) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (supplierProductName == null)
		{
			if (other.supplierProductName != null)
				return false;
		}
		else if (!supplierProductName.equals(other.supplierProductName))
			return false;
		return true;
	}
}

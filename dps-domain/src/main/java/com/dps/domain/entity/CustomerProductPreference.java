package com.dps.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class stores the customer specific product details.
 *
 * @see
 *
 * @Date Jul 16, 2016
 *
 * @author akshay
 */
@Entity
@Table(name = "DPS_CUST_PROD_PREF")
//@SequenceGenerator(name = "DPS_CUST_PROD_PREF_SEQ", sequenceName = "DPS_CUST_PROD_PREF_SEQ", initialValue = 1, allocationSize = 1)
public class CustomerProductPreference extends EntityBase
{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPS_CUST_PROD_PREF_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PROD_ID")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="CUST_ID")
	private Customer customer;
	
	@Basic
	private BigDecimal discount;

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public Long getId()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		CustomerProductPreference other = (CustomerProductPreference) obj;
		if (customer == null)
		{
			if (other.customer != null)
				return false;
		}
		else if (!customer.equals(other.customer))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (product == null)
		{
			if (other.product != null)
				return false;
		}
		else if (!product.equals(other.product))
			return false;
		return true;
	}
	
}

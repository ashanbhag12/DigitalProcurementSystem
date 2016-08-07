package com.dps.domain.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dps.domain.constants.CustomerOrderDetailStatus;

/**
 * This class holds customer order line items information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name = "DPS_CUST_ORDR_DETL")
//@SequenceGenerator(name = "DPS_CUST_ORDR_DETL_SEQ", sequenceName = "DPS_CUST_ORDR_DETL_SEQ", initialValue = 1, allocationSize = 1)
public class CustomerOrderDetails extends EntityBase
{
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPS_CUST_ORDR_DETL_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CUST_ORDR_ID")
	private CustomerOrder customerOrder;

	@ManyToOne
	@JoinColumn(name = "PROD_ID")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "SUPP_ID")
	private Supplier supplier;

	@Basic
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	private CustomerOrderDetailStatus status = CustomerOrderDetailStatus.NOT_ORDERED;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDR_PLCD_DATE")
	private Date orderPlacedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDR_RCVD_DATE")
	private Date orderReceivedDate;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public CustomerOrder getCustomerOrder()
	{
		return customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder)
	{
		this.customerOrder = customerOrder;
	}

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

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public CustomerOrderDetailStatus getStatus()
	{
		return status;
	}

	public void setStatus(CustomerOrderDetailStatus status)
	{
		this.status = status;
	}

	public Date getOrderPlacedDate()
	{
		return orderPlacedDate;
	}

	public void setOrderPlacedDate(Date orderPlacedDate)
	{
		this.orderPlacedDate = orderPlacedDate;
	}

	public Date getOrderReceivedDate()
	{
		return orderReceivedDate;
	}

	public void setOrderReceivedDate(Date orderReceivedDate)
	{
		this.orderReceivedDate = orderReceivedDate;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerOrder == null) ? 0 : customerOrder.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((orderPlacedDate == null) ? 0 : orderPlacedDate.hashCode());
		result = prime
				* result
				+ ((orderReceivedDate == null) ? 0 : orderReceivedDate
						.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((supplier == null) ? 0 : supplier.hashCode());
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
		CustomerOrderDetails other = (CustomerOrderDetails) obj;
		if (customerOrder == null)
		{
			if (other.customerOrder != null)
				return false;
		}
		else if (!customerOrder.equals(other.customerOrder))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (orderPlacedDate == null)
		{
			if (other.orderPlacedDate != null)
				return false;
		}
		else if (!orderPlacedDate.equals(other.orderPlacedDate))
			return false;
		if (orderReceivedDate == null)
		{
			if (other.orderReceivedDate != null)
				return false;
		}
		else if (!orderReceivedDate.equals(other.orderReceivedDate))
			return false;
		if (product == null)
		{
			if (other.product != null)
				return false;
		}
		else if (!product.equals(other.product))
			return false;
		if (quantity == null)
		{
			if (other.quantity != null)
				return false;
		}
		else if (!quantity.equals(other.quantity))
			return false;
		if (status != other.status)
			return false;
		if (supplier == null)
		{
			if (other.supplier != null)
				return false;
		}
		else if (!supplier.equals(other.supplier))
			return false;
		return true;
	}
}

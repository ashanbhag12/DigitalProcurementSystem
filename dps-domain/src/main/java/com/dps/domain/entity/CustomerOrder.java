package com.dps.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
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

import com.dps.domain.constants.CustomerOrderStatus;

/**
 * This class holds order information that the customers have placed.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name="DPS_CUST_ORDR")
//@SequenceGenerator(name="DPS_CUST_ORDR_SEQ", sequenceName="DPS_CUST_ORDR_SEQ", initialValue=1, allocationSize=1)
public class CustomerOrder extends EntityBase
{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DPS_CUST_ORDR_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="CUST_ID")
	private Customer customer;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderCompletedDate;
	
	@Enumerated(EnumType.STRING)
	private CustomerOrderStatus Status = CustomerOrderStatus.NO_ITEMS_ORDERED;
	
	@Basic
	private BigDecimal exchangeRate;

	@Override
	public Long getId()
	{
		return id;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public Date getOrderCompletedDate()
	{
		return orderCompletedDate;
	}

	public void setOrderCompletedDate(Date orderCompletedDate)
	{
		this.orderCompletedDate = orderCompletedDate;
	}

	public CustomerOrderStatus getStatus()
	{
		return Status;
	}

	public void setStatus(CustomerOrderStatus status)
	{
		Status = status;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public BigDecimal getExchangeRate()
	{
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate)
	{
		this.exchangeRate = exchangeRate;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Status == null) ? 0 : Status.hashCode());
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((orderCompletedDate == null) ? 0 : orderCompletedDate
						.hashCode());
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
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
		CustomerOrder other = (CustomerOrder) obj;
		if (Status != other.Status)
			return false;
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
		if (orderCompletedDate == null)
		{
			if (other.orderCompletedDate != null)
				return false;
		}
		else if (!orderCompletedDate.equals(other.orderCompletedDate))
			return false;
		if (orderDate == null)
		{
			if (other.orderDate != null)
				return false;
		}
		else if (!orderDate.equals(other.orderDate))
			return false;
		return true;
	}
	
}

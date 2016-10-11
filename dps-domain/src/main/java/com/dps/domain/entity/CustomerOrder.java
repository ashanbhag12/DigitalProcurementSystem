package com.dps.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
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
public class CustomerOrder extends EntityBase
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@TableGenerator(name="DPS_CUST_ORDR_ID", table="DPS_ID_GEN", pkColumnName="GEN_NAME",
					valueColumnName="GEN_VAL", pkColumnValue="DPS_CUST_ORDR_ID", allocationSize=10)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="DPS_CUST_ORDR_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="CUST_ID")
	private Customer customer;
	
	@OneToMany(mappedBy="customerOrder", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CustomerOrderDetails> lineItems;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDER_DATE")
	private Date orderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDER_COMPLETED_DATE")
	private Date orderCompletedDate;
	
	@Enumerated(EnumType.STRING)
	private CustomerOrderStatus Status = CustomerOrderStatus.NO_ITEMS_ORDERED;
	
	@Column(name="FX_RATE")
	private BigDecimal exchangeRate;
	
	@Column(name="WEIGHT_RATE")
	private BigDecimal weightRate;
	
	@Column(name="CBM_RATE")
	private BigDecimal cbmRate;

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

	public BigDecimal getWeightRate()
	{
		return weightRate;
	}

	public void setWeightRate(BigDecimal weightRate)
	{
		this.weightRate = weightRate;
	}

	public BigDecimal getCbmRate()
	{
		return cbmRate;
	}

	public void setCbmRate(BigDecimal cbmRate)
	{
		this.cbmRate = cbmRate;
	}

	public List<CustomerOrderDetails> getLineItems()
	{
		return lineItems;
	}

	public void setLineItems(List<CustomerOrderDetails> lineItems)
	{
		this.lineItems = lineItems;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

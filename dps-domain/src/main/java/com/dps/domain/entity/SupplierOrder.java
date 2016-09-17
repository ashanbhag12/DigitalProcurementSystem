package com.dps.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dps.domain.constants.SupplierOrderStatus;

/**
 * This class holds order information that the have placed to suppliers.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name="DPS_SUPP_ORDR")
public class SupplierOrder extends EntityBase
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="SUPP_ID")
	private Supplier supplier;
	
	@OneToMany(mappedBy="supplierOrder", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<SupplierOrderDetails> supplierOrderDetails = new ArrayList<>();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDER_DATE")
	private Date orderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORDER_COMPLETED_DATE")
	private Date orderCompletedDate;
	
	@Enumerated(EnumType.STRING)
	private SupplierOrderStatus status = SupplierOrderStatus.ORDER_PLACED;
	
	@Column(name="FX_RATE")
	private BigDecimal exchangeRate;
	
	@Column(name="WEIGHT_RATE")
	private BigDecimal weightRate;
	
	@Column(name="CBM_RATE")
	private BigDecimal cbmRate;

	public Long getId()
	{
		return id;
	}

	public Supplier getSupplier()
	{
		return supplier;
	}

	public void setSupplier(Supplier supplier)
	{
		this.supplier = supplier;
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

	public SupplierOrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(SupplierOrderStatus status)
	{
		this.status = status;
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

	public List<SupplierOrderDetails> getSupplierOrderDetails()
	{
		return supplierOrderDetails;
	}

	public void setSupplierOrderDetails(
			List<SupplierOrderDetails> supplierOrderDetails)
	{
		this.supplierOrderDetails = supplierOrderDetails;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((orderCompletedDate == null) ? 0 : orderCompletedDate
						.hashCode());
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
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
		SupplierOrder other = (SupplierOrder) obj;
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

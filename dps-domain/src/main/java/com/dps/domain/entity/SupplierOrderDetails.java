package com.dps.domain.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dps.domain.constants.SupplierOrderDetailStatus;

/**
 * This class holds supplier order line items information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name = "DPS_SUPP_ORDR_DETL")
public class SupplierOrderDetails extends EntityBase
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "SUPP_ORDR_ID")
	private SupplierOrder supplierOrder;
	
	@OneToOne
	@JoinColumn(name="CUST_ORDER_DETL_ID")
	private CustomerOrderDetails customerOrderDetails;
	
	@Enumerated(EnumType.STRING)
	private SupplierOrderDetailStatus status = SupplierOrderDetailStatus.ITEM_NOT_RECEIVED;

	public Long getId()
	{
		return id;
	}

	public SupplierOrder getSupplierOrder()
	{
		return supplierOrder;
	}

	public void setSupplierOrder(SupplierOrder supplierOrder)
	{
		this.supplierOrder = supplierOrder;
	}

	public CustomerOrderDetails getCustomerOrderDetails()
	{
		return customerOrderDetails;
	}

	public void setCustomerOrderDetails(CustomerOrderDetails customerOrderDetails)
	{
		this.customerOrderDetails = customerOrderDetails;
	}

	public SupplierOrderDetailStatus getStatus()
	{
		return status;
	}

	public void setStatus(SupplierOrderDetailStatus status)
	{
		this.status = status;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((customerOrderDetails == null) ? 0 : customerOrderDetails
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((supplierOrder == null) ? 0 : supplierOrder.hashCode());
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
		SupplierOrderDetails other = (SupplierOrderDetails) obj;
		if (customerOrderDetails == null)
		{
			if (other.customerOrderDetails != null)
				return false;
		}
		else if (!customerOrderDetails.equals(other.customerOrderDetails))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (supplierOrder == null)
		{
			if (other.supplierOrder != null)
				return false;
		}
		else if (!supplierOrder.equals(other.supplierOrder))
			return false;
		return true;
	}
}

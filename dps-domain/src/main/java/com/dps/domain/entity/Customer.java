package com.dps.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.dps.commons.domain.Constants;

/**
 * This class holds customer information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name = "DPS_CUST")
@NamedQueries({
	@NamedQuery(name=Customer.FIND_CUSTOMER_BY_NAME, query="SELECT C.id FROM Customer C where C.name like :name and C.isActive = 1"),
	@NamedQuery(name=Customer.FIND_CUSTOMER_BY_SHIPMARK, query = "SELECT C.id FROM Customer C where C.shipmark = :shipmark and C.isActive = 1"),
	@NamedQuery(name=Customer.GET_ALL_CUSTOMER_SHIPMARK, query="SELECT C.shipmark from Customer C where C.isActive = 1"),
	@NamedQuery(name=Customer.GET_CUSTOMER_COUNT, query="SELECT Count(c) from Customer c where c.isActive = 1"),
	@NamedQuery(name=Customer.GET_ALL_CUSTOMERS, query="SELECT C.id from Customer C where C.isActive = 1")
})
public class Customer extends EntityBase
{
	private static final long serialVersionUID = 1L;
	public static final String FIND_CUSTOMER_BY_NAME = "Customer.FindCustomerByName";
	public static final String FIND_CUSTOMER_BY_SHIPMARK = "Customer.FindCustomerByShipmark";
	public static final String GET_ALL_CUSTOMER_SHIPMARK = "Customer.GetAllCustomerShipmark";
	public static final String GET_CUSTOMER_COUNT = "Customer.getCustomerCount";
	public static final String GET_ALL_CUSTOMERS = "Customer.getAllCustomers";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic
	private String name;

	@Basic
	private String shipmark;
	
	@Basic
	@Column(name="ADDITIONAL_MARGIN")
	private BigDecimal additionalMargin = Constants.BIG_DECIMAL_ONE;

	@Embedded
	private Address address;
	
	@Embedded
	private ContactDetails contactDetails;
	
	@Basic
	private Boolean isActive;

	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public ContactDetails getContactDetails()
	{
		return contactDetails;
	}

	public void setContactDetails(ContactDetails contactDetails)
	{
		this.contactDetails = contactDetails;
	}

	public String getShipmark()
	{
		return shipmark;
	}

	public void setShipmark(String shipmark)
	{
		this.shipmark = shipmark;
	}

	public BigDecimal getAdditionalMargin()
	{
		return additionalMargin;
	}

	public void setAdditionalMargin(BigDecimal additionalMargin)
	{
		this.additionalMargin = additionalMargin;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result	+ ((shipmark == null) ? 0 : shipmark.hashCode());
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
		Customer other = (Customer) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (shipmark == null)
		{
			if (other.shipmark != null)
				return false;
		}
		else if (!shipmark.equals(other.shipmark))
			return false;
		return true;
	}
}
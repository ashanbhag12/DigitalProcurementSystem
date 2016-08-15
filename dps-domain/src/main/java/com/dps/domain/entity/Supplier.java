package com.dps.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * This class holds supplier information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */

@Entity
@Table(name = "DPS_SUPP")
//@SequenceGenerator(name = "DPS_SUPP_SEQ", sequenceName = "DPS_SUPP_SEQ", initialValue = 1, allocationSize = 1)
@NamedQueries({
	@NamedQuery(name=Supplier.FIND_SUPPLIER_BY_INITIALS, query="SELECT S.id FROM Supplier S WHERE S.initials = :initials"),
	@NamedQuery(name=Supplier.FIND_SUPPLIER_BY_NAME, query="SELECT S.id FROM Supplier S WHERE S.name LIKE :name"),
	@NamedQuery(name=Supplier.GET_ALL_SUPPLIER_INITIALS, query="SELECT S.initials FROM Supplier S"),
	@NamedQuery(name=Supplier.GET_SUPPLIER_COUNT, query="SELECT COUNT(S) from Supplier S")
})
public class Supplier extends EntityBase
{
	private static final long serialVersionUID = 1L;
	public static final String FIND_SUPPLIER_BY_INITIALS = "Supplier.FindSupplierByInitials";
	public static final String FIND_SUPPLIER_BY_NAME = "Supplier.FindSupplierByName";
	public static final String GET_ALL_SUPPLIER_INITIALS = "Supplier.GetAllSupplierInitials";
	public static final String GET_SUPPLIER_COUNT = "Supplier.GetSupplierCount";

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPS_SUPP_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic
	private String name;

	@Basic
	private String initials;

	@Embedded
	private ContactDetails contactDetails;
	
	@Override
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

	public String getInitials()
	{
		return initials;
	}

	public void setInitials(String initials)
	{
		this.initials = initials;
	}

	public ContactDetails getContactDetails()
	{
		return contactDetails;
	}

	public void setContactDetails(ContactDetails contactDetails)
	{
		this.contactDetails = contactDetails;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((initials == null) ? 0 : initials.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Supplier other = (Supplier) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (initials == null)
		{
			if (other.initials != null)
				return false;
		}
		else if (!initials.equals(other.initials))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}
}

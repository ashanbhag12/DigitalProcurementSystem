package com.dps.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class holds address information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Embeddable
public class Address
{
	@Column(name = "FLAT_NO")
	private String flatNo;

	@Basic
	private String building;

	@Basic
	private String street;

	@Basic
	private String locality;

	@Basic
	private String city;

	@Basic
	private String state;

	@Basic
	private String zip;

	public String getFlatNo()
	{
		return flatNo;
	}

	public void setFlatNo(String flatNo)
	{
		this.flatNo = flatNo;
	}

	public String getBuilding()
	{
		return building;
	}

	public void setBuilding(String building)
	{
		this.building = building;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getLocality()
	{
		return locality;
	}

	public void setLocality(String locality)
	{
		this.locality = locality;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((building == null) ? 0 : building.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((flatNo == null) ? 0 : flatNo.hashCode());
		result = prime * result	+ ((locality == null) ? 0 : locality.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		Address other = (Address) obj;
		if (building == null)
		{
			if (other.building != null)
				return false;
		}
		else if (!building.equals(other.building))
			return false;
		if (city == null)
		{
			if (other.city != null)
				return false;
		}
		else if (!city.equals(other.city))
			return false;
		if (flatNo == null)
		{
			if (other.flatNo != null)
				return false;
		}
		else if (!flatNo.equals(other.flatNo))
			return false;
		if (locality == null)
		{
			if (other.locality != null)
				return false;
		}
		else if (!locality.equals(other.locality))
			return false;
		if (state == null)
		{
			if (other.state != null)
				return false;
		}
		else if (!state.equals(other.state))
			return false;
		if (street == null)
		{
			if (other.street != null)
				return false;
		}
		else if (!street.equals(other.street))
			return false;
		if (zip == null)
		{
			if (other.zip != null)
				return false;
		}
		else if (!zip.equals(other.zip))
			return false;
		return true;
	}
}

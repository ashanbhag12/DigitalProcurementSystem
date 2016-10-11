package com.dps.web.service.model;

import java.math.BigDecimal;

/**
 * The simple customer entity that will be sent from the UI form.
 *
 * @see
 *
 * @Date Jul 23, 2016
 *
 * @author akshay
 */
public class CustomerDTO
{
	private Long id;
	private String name;
	private String shipmark;
	private String originalShipmark;
	private BigDecimal additionalMargin;
	private BigDecimal additionalMarginPercentage;
	private String flatNo;
	private String building;
	private String street;
	private String locality;
	private String city;
	private String state;
	private String zip;
	private String emailId;
	private String phoneNumber;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public String getEmailId()
	{
		return emailId;
	}

	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public BigDecimal getAdditionalMarginPercentage()
	{
		return additionalMarginPercentage;
	}

	public void setAdditionalMarginPercentage(BigDecimal additionalMarginPercentage)
	{
		this.additionalMarginPercentage = additionalMarginPercentage;
	}

	public String getOriginalShipmark()
	{
		return originalShipmark;
	}

	public void setOriginalShipmark(String originalShipmark)
	{
		this.originalShipmark = originalShipmark;
	}
}

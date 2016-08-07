package com.dps.web.service.model;

/**
 * The simple supplier entity that will be sent from the UI form.
 *
 * @see
 *
 * @Date Jul 23, 2016
 *
 * @author akshay
 */
public class SupplierDTO
{
	private Long id;
	private String name;
	private String initials;
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

	public String getInitials()
	{
		return initials;
	}

	public void setInitials(String initials)
	{
		this.initials = initials;
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
}

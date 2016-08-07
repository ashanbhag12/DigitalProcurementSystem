package com.dps.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Embeddable class that will store the email id and the phone number information.
 *
 * @see
 *
 * @Date Jul 16, 2016
 *
 * @author akshay
 */
@Embeddable
public class ContactDetails
{
	@Basic
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Basic
	@Column(name="PHONE_NO")
	private String phoneNumber;

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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		ContactDetails other = (ContactDetails) obj;
		if (emailId == null)
		{
			if (other.emailId != null)
				return false;
		}
		else if (!emailId.equals(other.emailId))
			return false;
		if (phoneNumber == null)
		{
			if (other.phoneNumber != null)
				return false;
		}
		else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}
}

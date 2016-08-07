package com.dps.commons.domain;

import java.io.Serializable;

/**
 * Encapsulated class for primary key of all jpa entities.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class JpaEntityId implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public JpaEntityId(Long id)
	{
		this.id = id;
	}
	
	public Long getId()
	{
		return id;
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
		JpaEntityId other = (JpaEntityId) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else
			if (!id.equals(other.id))
				return false;
		return true;
	}
}

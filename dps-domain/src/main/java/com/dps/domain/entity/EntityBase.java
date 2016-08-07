package com.dps.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dps.domain.entity.listener.TimestampUpdater;

/**
 * Base class for all entities. Will hold attributes that are common across jpa
 * entities.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@MappedSuperclass
@EntityListeners(TimestampUpdater.class)
public abstract class EntityBase implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "CREATION_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "LAST_MODIFIED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;

	/**
	 * Returns the id (primary key) of the entity.
	 * 
	 * @return The id (primary key) of the entity.
	 */
	public abstract Long getId();

	public Date getCreatedTime()
	{
		return createdTime;
	}

	public void setCreatedTime(Date createdTime)
	{
		this.createdTime = createdTime;
	}

	public Date getLastModifiedTime()
	{
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime)
	{
		this.lastModifiedTime = lastModifiedTime;
	}
}

package com.dps.domain.entity.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.dps.domain.entity.EntityBase;

/**
 * Updates the last modified and created time before persisting the entity.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class TimestampUpdater
{
	/**
	 * Populates the last change time stamp and created time stamp with the system date of the entity being persisted.
	 * @param entity - The entity object being persisted.
	 */
	@PrePersist
	public void prePersist(EntityBase entity)
	{
		Date date = new Date();
		entity.setCreatedTime(date);
		entity.setLastModifiedTime(date);
	}
	
	/**
	 * Updates the last change time stamp to the system date of the entity being updated.
	 * @param entity - The entity object being updated.
	 */
	@PreUpdate
	public void preUpdate(EntityBase entity)
	{
		Date date = new Date();
		entity.setLastModifiedTime(date);
	}
}

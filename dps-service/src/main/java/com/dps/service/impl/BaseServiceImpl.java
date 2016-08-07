package com.dps.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dps.cache.Cache;
import com.dps.commons.domain.JpaEntityId;
import com.dps.dao.BaseDao;
import com.dps.domain.entity.EntityBase;
import com.dps.service.BaseService;

/**
 * Default implementation of {@link BaseService} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public abstract class BaseServiceImpl<E extends EntityBase> implements BaseService<E>
{
	private BaseDao<E> dao;
	
	protected Cache<Long, E> cache;
	
	private Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
	public void setDao(BaseDao<E> dao)
	{
		this.dao = dao;
	}
	
	/**
	 *  Concrete Service implementations that will extend from this class will
	 *  use this method to set the appropriate dao object, and instantiate the cache object 
	 *  by selecting the appropriate size of the objects to be cached.
	 */
	protected abstract void init();
	
	protected abstract void initializeCache();

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void persist(E entity)
	{
		try
		{
			dao.persist(entity);
			cache.put(entity.getId(), entity);
		}
		catch(EntityExistsException e)
		{
			String message = "The " + entity.getClass().getSimpleName() + " entity with id: " + 
					entity.getId() +" already exixts in the database";
			logger.error(message);
			
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to persist " + entity.getClass().getSimpleName() + 
					" entity having id : " + entity.getId();
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void persistAll(Collection<E> entityCollection)
	{
		try
		{
			dao.persistAll(entityCollection);
			for(E entity : entityCollection)
			{
				cache.put(entity.getId(), entity);
			}
		}
		catch(EntityExistsException e)
		{
			String message = "The entity already exixts in the database";
			logger.error(message);
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to persist entity.";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E merge(E entity)
	{
		try
		{
			E mergedEntity =  dao.merge(entity);
			cache.invalidate(entity.getId());
			cache.put(mergedEntity.getId(), mergedEntity);
			return mergedEntity;
		}
		catch(IllegalArgumentException e)
		{
			String message = "The " + entity.getClass().getSimpleName() + " entity with id: " + 
					entity.getId() +" is not found in the database";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to merge " + entity.getClass().getSimpleName() + 
					" entity having id : " + entity.getId();
			logger.error(message);
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> mergeAll(Collection<E> entityCollection)
	{
		try
		{
			List<E> mergedEntityList = dao.mergeAll(entityCollection);
			
			for(E e : mergedEntityList)
			{
				cache.invalidate(e.getId());
				cache.put(e.getId(), e);
			}
			
			return mergedEntityList;
		}
		catch(IllegalArgumentException e)
		{
			String message = "The entity is not found in the database";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to merge entity.";
			logger.error(message);
		}
		throw new RuntimeException();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(JpaEntityId id)
	{
		try
		{
			dao.remove(id);
			cache.invalidate(id.getId());
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to delete entity.";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void removeAll(Collection<JpaEntityId> idCollection)
	{
		try
		{
			dao.removeAll(idCollection);
			for(JpaEntityId id : idCollection)
			{
				cache.invalidate(id.getId());
			}
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to delete entity.";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void detach(E entity)
	{
		try
		{
			dao.detach(entity);
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void detachAll(Collection<E> entityCollection)
	{
		try
		{
			dao.detachAll(entityCollection);
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refresh(E entity)
	{
		try
		{
			dao.refresh(entity);
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to refresh " + entity.getClass().getSimpleName() + 
					" entity having id : " + entity.getId();
			logger.error(message);
		}
		catch(EntityNotFoundException e)
		{
			String message = "The " + entity.getClass().getSimpleName() + " entity with id: " + 
					entity.getId() +" is not found in the database";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshAll(Collection<E> entityColletion)
	{
		try
		{
			dao.refreshAll(entityColletion);
		}
		catch(IllegalArgumentException e)
		{
			String message = "The object passed is not an entity";
			logger.error(message);
		}
		catch(TransactionRequiredException e)
		{
			String message = "Transaction not present while trying to delete entity.";
			logger.error(message);
		}
		catch(EntityNotFoundException e)
		{
			String message = "The entity is not found in the database";
			logger.error(message);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E find(JpaEntityId id)
	{
		try
		{
			E e = cache.get(id.getId());
			
			if(e == null)
			{
				e = fetchFromDatabase(id);
			}
			
			return e;
		}
		catch(IllegalArgumentException e)
		{
			String message = "The id passed is null";
			logger.error(message);
			throw e;
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAll(Collection<JpaEntityId> idCollection)
	{
		List<JpaEntityId> objectsToBeFetchedFromDatabase = new ArrayList<>();
		List<E> finalObjectList = new ArrayList<>();
		
		for(JpaEntityId jpaEntityId : idCollection)
		{
			E entity = cache.get(jpaEntityId.getId());
			
			if(entity == null)
			{
				objectsToBeFetchedFromDatabase.add(jpaEntityId);
			}
			else
			{
				finalObjectList.add(entity);
			}
		}
		
		if(!objectsToBeFetchedFromDatabase.isEmpty())
		{
			List<E> nonCachedEntities = fetchAllFromDatabase(objectsToBeFetchedFromDatabase);
			for(E entity : nonCachedEntities)
			{
				cache.put(entity.getId(), entity);
			}
			finalObjectList.addAll(nonCachedEntities);
		}
		
		return finalObjectList;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAll()
	{
		List<JpaEntityId> idList = dao.findAll();
		
		return findAll(idList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAll(int startIndex, int maxResults)
	{
		List<JpaEntityId> idList = dao.findAll(startIndex, maxResults);
		
		return findAll(idList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E findByNamedQuery(String namedQueryName, Map<String, ?> parameters)
	{
		JpaEntityId id = dao.findByNamedQuery(namedQueryName, parameters);
		
		return find(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E findByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType)
	{
		JpaEntityId id = dao.findByNamedQuery(namedQueryName, parameters, flushModeType);
		
		return find(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters)
	{
		List<JpaEntityId> idList = dao.findAllByNamedQuery(namedQueryName, parameters);
		
		return findAll(idList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType)
	{
		List<JpaEntityId> idList =  dao.findAllByNamedQuery(namedQueryName, parameters, flushModeType);
		
		return findAll(idList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters, int startIndex, int maxResults)
	{
		List<JpaEntityId> idList = dao.findAllByNamedQuery(namedQueryName, parameters, startIndex, maxResults);
		
		return findAll(idList);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void flush()
	{
		dao.flush();
	}
	
	/**
	 * Fetches all the specified objects from the database.
	 * @param idCollection - {@link Collection} of ids of object to be fetched from the database.
	 * @return {@link List}of objects fetched from the database.
	 */
	private List<E> fetchAllFromDatabase(Collection<JpaEntityId> idCollection)
	{
		return dao.findAll(idCollection);
	}
	
	/**
	 * Fetches the required object from the database.
	 * @param id - Id of the object to be fetched from the database.
	 * @return - The object returned from the database.
	 */
	private E fetchFromDatabase(JpaEntityId id)
	{
		E entity = null;
		
		entity = dao.find(id);
		
		return entity;
	}
}

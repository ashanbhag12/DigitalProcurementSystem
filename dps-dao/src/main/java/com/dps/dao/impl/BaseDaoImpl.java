package com.dps.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dps.commons.domain.JpaEntityId;
import com.dps.dao.BaseDao;
import com.dps.domain.entity.EntityBase;

/**
 * Generic implementation of {@link BaseDao} interface.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public abstract class BaseDaoImpl<E extends EntityBase> implements BaseDao<E>
{
	private static final int BATCH_SIZE = 10000;
	protected final Class<E> entityClass;
	
	@PersistenceContext(unitName="DPSPersistenceUnit")
	protected EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl()
	{
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>)genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void persist(E entity)
	{
		entityManager.persist(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void persistAll(Collection<E> entityCollection)
	{
		int counter = 0;
		for(E entity : entityCollection)
		{
			persist(entity);
			
			if(++counter % BATCH_SIZE == 0)
			{
				entityManager.flush();
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E merge(E entity)
	{
		E e = entityManager.merge(entity);
		return e;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> mergeAll(Collection<E> entityCollection)
	{
		List<E> list = new ArrayList<>();
		
		for(E entity : entityCollection)
		{
			E e = merge(entity);
			list.add(e);
		}
		return list;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(JpaEntityId id)
	{
		Long pk = id.getId();
		
		E entity = entityManager.find(entityClass, pk);
		entityManager.remove(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void removeAll(Collection<JpaEntityId> idCollection)
	{
		int counter = 0;
		for(JpaEntityId id : idCollection)
		{
			remove(id);
			
			if(++counter % BATCH_SIZE == 0)
			{
				entityManager.flush();
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void detach(E entity)
	{
		entityManager.detach(entity);
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void detachAll(Collection<E> entityCollection)
	{
		for(E entity : entityCollection)
		{
			detach(entity);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refresh(E entity)
	{
		entityManager.refresh(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshAll(Collection<E> entityColletion)
	{
		for(E entity : entityColletion)
		{
			refresh(entity);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public E find(JpaEntityId id)
	{
		Long pk = id.getId();
		return entityManager.find(entityClass, pk);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<E> findAll(Collection<JpaEntityId> idCollection)
	{
		List<E> list = new ArrayList<>();
		
		for(JpaEntityId id : idCollection)
		{
			E entity = find(id);
			list.add(entity);
		}
		
		return list;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAll()
	{
		String jpqlQuery = "SELECT E.id from " + entityClass.getSimpleName() + " E";
		TypedQuery<Long> typedQuery = entityManager.createQuery(jpqlQuery, Long.class);
		
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAll(int startIndex, int maxResults)
	{
		String jpqlQuery = "SELECT E.id from " + entityClass.getSimpleName() + " E ORDER BY E.id";
		
		TypedQuery<Long> typedQuery = entityManager.createQuery(jpqlQuery, Long.class);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(maxResults);
		
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public JpaEntityId findByNamedQuery(String namedQueryName, Map<String, ?> parameters)
	{
		return findByNamedQuery(namedQueryName, parameters, null);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public JpaEntityId findByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType)
	{
		TypedQuery<Long> typedQuery = createTypedQuery(namedQueryName, parameters, flushModeType);
		
		return new JpaEntityId(typedQuery.getSingleResult());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters)
	{
		return findAllByNamedQuery(namedQueryName, parameters, null);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType)
	{
		TypedQuery<Long> typedQuery = createTypedQuery(namedQueryName, parameters, flushModeType);
		List<Long> resultList = typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<JpaEntityId> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters, int startIndex, int endIndex)
	{
		TypedQuery<Long> typedQuery = createTypedQuery(namedQueryName, parameters, null);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(endIndex);
		
		List<Long> resultList =  typedQuery.getResultList();
		
		List<JpaEntityId> idList = new ArrayList<>();
		for(Long i : resultList)
		{
			idList.add(new JpaEntityId(i));
		}
		
		return idList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void flush()
	{
		entityManager.flush();
	}
	
	private TypedQuery<Long> createTypedQuery(String name, Map<String, ?> parameters, FlushModeType flushMode)
	{
		//Add validation for query name and parameters.
		
		TypedQuery<Long> typedQuery = entityManager.createNamedQuery(name, Long.class);
		
		//Add flush mode if it is not null
		if(flushMode != null)
		{
			typedQuery.setFlushMode(flushMode);
		}
		
		//Set the named parameters in named query
		for(Map.Entry<String, ?> param : parameters.entrySet())
		{
			typedQuery.setParameter(param.getKey(), param.getValue());
		}
		
		return typedQuery;
	}

}

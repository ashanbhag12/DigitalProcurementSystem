package com.dps.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.EntityBase;

/**
 * Base service interface. This interface will be extended by all service interfaces.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface BaseService<E extends EntityBase>
{
	/**
	 * Persist the specified entity object.
	 * @param entity - entity instance
	 */
	public void persist(E entity);
	
	/**
	 * Persists the given {@link Collection} of entity objects.
	 * @param entityCollection - {@link Collection} of entity instances.
	 */
	public void persistAll(Collection<E> entityCollection);
	
	/**
	 * Merges the state of the given entities into the current persistence context.
	 * @param entity - Entity instance that needs to be merged.
	 * @return The managed instance that the state was merged to.
	 */
	public E merge(E entity);
	
	/**
	 * Merges the state of the given entities into the current persistence context.
	 * @param entityCollection - {@link Collection} of entity instances that needs to be merged to the current persistence context.
	 * @return {@link List} of managed entity instances.
	 */
	public List<E> mergeAll(Collection<E> entityCollection);
	
	/**
	 * Removes the entity corresponding to the specified id.
	 * @param id - Id of the entity to be removed.
	 */
	public void remove(JpaEntityId id);
	
	/**
	 * Removes the entities corresponding to the specified id collection.
	 * @param idCollection - {@link Collection} of id of entities that are needed to be removed.
	 */
	public void removeAll(Collection<JpaEntityId> idCollection);
	
	/**
	 * Remove the given entity from the persistence context, causing a managed entity to become detached.
	 * Unflushed changes made to the entity if any (including removal of the entity), will not be synchronized to the database.
	 * Entities which previously referenced the detached entity will continue to reference it.
	 * @param entity - entity instance
	 */
	public void detach(E entity);
	
	/**
	 * Removes all the given entities from the persistence context, causing the managed entities to become detached.
	 * Unflushed changes made to the entities if any (including removal of the entity), will not be synchronized to the database.
	 * Entities which previously referenced the detached entity will continue to reference it.
	 * @param entityCollection - {@link Collection} of entity instances.
	 */
	public void detachAll(Collection<E> entityCollection);
	
	/**
	 * Refreshes the state of the instance from the database, overwriting changes made to the entity, if any.
	 * @param entity - entity instance.
	 */
	public void refresh(E entity);
	
	/**
	 * Refreshes the state of all the instances in the {@link Collection} from the database, overwriting changes made to the entity, if any.
	 * @param entityColletion - {@link Collection} of entity instances.
	 */
	public void refreshAll(Collection<E> entityColletion);
	
	/**
	 * Finds the entity instance by id (primary key).
	 * If the entity instance is contained in the persistence context, it is returned from there.
	 * @param id primary key. It must not be null.
	 * @return The found entity instance or null if the entity does not exist.
	 */
	public E find(JpaEntityId id);
	
	/**
	 * Finds entity instances whose id's (primary keys) are present in the specified {@link Collection}.
	 * @param idCollection {@link Collection} of id's (primary keys) of entities that are to be fetched.
	 * @return {@link List} of entity instances.
	 */
	public List<E> findAll(Collection<JpaEntityId> idCollection);
	
	/**
	 * Finds all the entity instances from the database.
	 * @return {@link List} of entity instances.
	 */
	public List<E> findAll();
	
	/**
	 * Finds all the entity instances from the database but returns the list of entity instances
	 * starting from the specified start index and at max the specified number of instances.
	 * @param startIndex - start index of the results
	 * @param maxResults - maximum number of entity instances to be returned in the result
	 * @return {@link List} of entity instances based on the specified start index and max number of results
	 */
	public List<E> findAll(int startIndex, int maxResults);
	
	/**
	 * Finds an entity instance by executing the specified query against the database.
	 * @param namedQueryName - Name of the named query containing named parameters.
	 * @param parameters - Values of all the named parameters used in the query  
	 * @return The found entity.
	 */
	public E findByNamedQuery(String namedQueryName, Map<String, ?> parameters);
	
	/**
	 * Finds an entity instance by executing the specified query against the database.
	 * @param namedQueryName - Name of the named query containing named parameters.
	 * @param parameters - Values of all the named parameters used in the query.
	 * @param flushModeType - Flush mode to be used (optional). Use Flush mode carefully. Incorrect usage might return incorrect entity instance with respect to the current persistent context/transaction.
	 * @return The found entity
	 */
	public E findByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType);
	
	/**
	 * Finds {@link List} of entity instances by executing the specified query against the database.
	 * @param namedQueryName - Name of the named query containing named parameters.
	 * @param parameters - Values of all the named parameters used in the query.
	 * @return The {@link List} of found entities
	 */
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters);
	
	/**
	 * Finds {@link List} of entity instances by executing the specified query against the database.
	 * @param namedQueryName - Name of the named query containing named parameters.
	 * @param parameters - Values of all the named parameters used in the query.
	 * @param flushModeType - Flush mode to be used (optional). Use Flush mode carefully. Incorrect usage might return incorrect entity instance with respect to the current persistent context/transaction.
	 * @return The {@link List} of found entities
	 */
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String, ?> parameters, FlushModeType flushModeType);
	
	/**
	 * Finds entity instances by executing the specified query against the database but returns the {@link List} of entity instances
	 * starting from the specified start index and max the specified number of instances.
	 * @param namedQueryName - Name of the named query containing named parameters.
	 * @param parameters - Values of all the named parameters used in the query.
	 * @param startIndex - start index of the results
	 * @param maxResults - maximum number of entity instances to be returned in the result
	 * @return {@link List} of entity instances based on the specified start index and max number of results
	 */
	public List<E> findAllByNamedQuery(String namedQueryName, Map<String,?> parameters, int startIndex, int maxResults);
	
	/**
	 * Synchronizes the persistence context to the underlying database
	 */
	public void flush();
}

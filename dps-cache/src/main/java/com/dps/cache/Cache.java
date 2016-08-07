package com.dps.cache;

import java.util.Collection;
import java.util.Map;

/**
 * Interface used to interact with the in memory cache.
 * Cache entries are manually added using put(key, value), and are stored in the cache 
 * until either evicted or manually invalidated.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public interface Cache<K,V>
{
	/**
	 * Returns the value associated with key in this cache if present, null otherwise.
	 * 
	 * @param key - The key of the object that needs to be fetched from the cache.
	 * @return The value associated with key in this cache if present, null otherwise.
	 */
	public V get(K key);
	
	/**
	 * Returns a {@link Map} of the values associated with keys in this cache. 
	 * The returned map will only contain entries which are already present in the cache.
	 * 
	 * @param keyCollection - {@link Collection} of keys for which values are to be fetched from the cache.
	 * @return Returns a {@link Map} of the keys and their associated values in this cache.
	 */
	public Map<K, V> getValuesPresent(Collection<K> keyCollection);
	
	/**
	 * Removes the key and the value stored against the specified key from the cache.
	 * 
	 * @param key - The key of the object to be removed from the cache.
	 */
	public void invalidate(K key);
	
	/**
	 * Removes all the keys and values that are stored in the cache.
	 */
	public void invalidateAll();
	
	/**
	 * Removes all the keys and their corresponding values specified in the keyCollection.
	 * 
	 * @param keyCollection - {@link Collection} of keys whose values needs to be removed from the cache.
	 */
	public void invalidateAll(Collection<K> keyCollection);
	
	/**
	 * Stores the value against the specified key in the cache.
	 * 
	 * @param key - The key against which the value is to be stored in the cache.
	 * @param value - The object to be stored in the cache.
	 */
	public void put(K key, V value);
	
	/**
	 * Stores each of the values against the specified keys in the cache.
	 * 
	 * @param entries - {@link Map} storing the key and values that are to be added to the cache.
	 */
	public void putAll(Map<K, V> entries);
	
	/**
	 * Returns the number of elements that are present in the cache.
	 * 
	 * @return The number of elements that are present in the cache.
	 */
	public int size();
}

package com.dps.cache.impl;

import com.dps.cache.Cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the Cache interface.
 * This implementation uses a {@link Map} to store the values.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class CacheImpl<K,V> implements Cache<K,V>
{
	private final Map<K, V> cache;
	
	public CacheImpl(int cacheSize)
	{
		super();
		cache = new DpsLocalCache<K,V>(cacheSize);
	}

	@Override
	public V get(K key)
	{
		return cache.get(key);
	}

	@Override
	public Map<K, V> getValuesPresent(Collection<K> keyCollection)
	{
		Map<K, V> values = new HashMap<>();
		
		for(K key : keyCollection)
		{
			V element = get(key);
			if(element != null)
			{
				values.put(key, element);
			}
		}
		return values;
	}

	@Override
	public void invalidate(K key)
	{
		cache.remove(key);
	}

	@Override
	public void invalidateAll()
	{
		cache.clear();
	}

	@Override
	public void invalidateAll(Collection<K> keyCollection)
	{
		for(K key : keyCollection)
		{
			invalidate(key);
		}
	}

	@Override
	public void put(K key, V value)
	{
		cache.put(key, value);
	}

	@Override
	public void putAll(Map<K, V> entries)
	{
		for(Map.Entry<K, V> elements : entries.entrySet())
		{
			put(elements.getKey(), elements.getValue());
		}
	}

	@Override
	public int size()
	{
		return cache.size();
	}

}


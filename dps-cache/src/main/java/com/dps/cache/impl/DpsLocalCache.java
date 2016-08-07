package com.dps.cache.impl;

import com.dps.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a simple implementation of a local cache.
 * The number of elements that will be cached can be restricted 
 * by passing the value to the constructor while creating instance of the cache. 
 * The objects will be cached in LRU fashion. i.e. the least recently used object
 * will be removed from the cache to make place for new element if the cache is full.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
public class DpsLocalCache<K,V> extends LinkedHashMap<K, V>
{
	private static final long serialVersionUID = 1L;
	
	private int maxElements;
	
	/**
	 * Default constructor provided. Should not be instantiated outside the package.
	 * For external use, implementations of {@link Cache} interface must be used.
	 */
	DpsLocalCache(int maxElements)
	{
		super(maxElements, 0.99f, true);
		this.maxElements = maxElements;
	}
	
	@Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) 
	{
        return size() > maxElements;
    }
}

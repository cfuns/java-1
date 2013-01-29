package de.benjaminborbe.cache.api;

public interface CacheService {

	void set(String key, String value) throws CacheServiceException;

	String get(String key) throws CacheServiceException;
}

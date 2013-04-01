package de.benjaminborbe.cache.service;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;

@Singleton
public class CacheServiceImpl implements CacheService {

	private final Map<String, String> data = new HashMap<String, String>();

	@Inject
	public CacheServiceImpl() {
	}

	@Override
	public void set(final String key, final String value) throws CacheServiceException {
		data.put(key, value);
	}

	@Override
	public String get(final String key) throws CacheServiceException {
		return data.get(key);
	}

}

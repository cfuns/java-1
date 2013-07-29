package de.benjaminborbe.cache.mock;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;

import java.util.HashMap;
import java.util.Map;

public class CacheServiceMock implements CacheService {

	private final Map<String, String> data = new HashMap<String, String>();

	public CacheServiceMock() {
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

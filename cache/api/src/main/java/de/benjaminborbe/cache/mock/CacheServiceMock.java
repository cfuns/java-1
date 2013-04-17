package de.benjaminborbe.cache.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CacheServiceMock implements CacheService {

	private final Map<String, String> data = new HashMap<>();

	@Inject
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

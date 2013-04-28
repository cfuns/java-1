package de.benjaminborbe.lib.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheSimple<Key, Value> {

	private final Map<Key, CacheEntry<Value>> data = new HashMap<Key, CacheEntry<Value>>();

	private final long maxAge = 30 * 1000; // 30 sec

	public Value get(final Key configurationDescription) {
		CacheEntry<Value> cacheEntry = data.get(configurationDescription);
		if (cacheEntry != null && cacheEntry.getExpire() > System.currentTimeMillis()) {
			return cacheEntry.getValue();
		} else {
			return null;
		}
	}

	public void put(final Key configurationDescription, final Value value) {
		data.put(configurationDescription, new CacheEntry<Value>(value, System.currentTimeMillis() + maxAge));
	}

	public void clear() {
		data.clear();
	}

}

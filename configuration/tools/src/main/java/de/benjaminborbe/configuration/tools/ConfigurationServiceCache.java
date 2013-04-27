package de.benjaminborbe.configuration.tools;

import de.benjaminborbe.configuration.api.ConfigurationDescription;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationServiceCache {

	private final Map<ConfigurationDescription, CacheEntry> data = new HashMap<ConfigurationDescription, CacheEntry>();

	private final long maxAge = 30 * 1000; // 30 sec

	@Inject
	public ConfigurationServiceCache() {
	}

	public String get(final ConfigurationDescription configurationDescription) {
		CacheEntry cacheEntry = data.get(configurationDescription);
		if (cacheEntry != null && cacheEntry.getExpire() > System.currentTimeMillis()) {
			return cacheEntry.getValue();
		} else {
			return null;
		}
	}

	public void put(final ConfigurationDescription configurationDescription, final String value) {
		data.put(configurationDescription, new CacheEntry(value));
	}

	private final class CacheEntry {

		private final String value;

		private final long expire;

		public CacheEntry(String value) {
			this.value = value;
			this.expire = System.currentTimeMillis() + 3600;
		}

		private long getExpire() {
			return expire;
		}

		private String getValue() {
			return value;
		}
	}
}

package de.benjaminborbe.storage.tools;

import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.map.MapChain;

public class StorageValueMap extends MapChain<StorageValue, StorageValue> {

	private static final long serialVersionUID = 4669984747118452873L;

	private final String encoding;

	public StorageValueMap(final String encoding) {
		this.encoding = encoding;
	}

	public StorageValueMap add(final String key, final String value) {
		this.put(new StorageValue(key, encoding), new StorageValue(value, encoding));
		return this;
	}

	@Override
	public StorageValueMap add(final StorageValue key, final StorageValue value) {
		this.put(key, value);
		return this;
	}

}

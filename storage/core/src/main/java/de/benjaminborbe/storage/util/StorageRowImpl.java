package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageValue;

import java.util.Collection;
import java.util.Map;

public class StorageRowImpl implements StorageRow {

	private final StorageValue key;

	private final Map<StorageValue, StorageValue> data;

	public StorageRowImpl(final StorageValue key, final Map<StorageValue, StorageValue> data) {
		this.key = key;
		this.data = data;
	}

	@Override
	public StorageValue getKey() {
		return key;
	}

	@Override
	public StorageValue getValue(final StorageValue columnName) {
		return data.get(columnName);
	}

	@Override
	public Collection<StorageValue> getColumnNames() {
		return data.keySet();
	}

}

package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageValue;

public class StorageColumnImpl implements StorageColumn {

	private final StorageValue name;

	private final StorageValue value;

	public StorageColumnImpl(final StorageValue name, final StorageValue value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public StorageValue getColumnName() {
		return name;
	}

	@Override
	public StorageValue getColumnValue() {
		return value;
	}

}

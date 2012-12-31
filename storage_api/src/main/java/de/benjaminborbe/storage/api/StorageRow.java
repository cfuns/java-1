package de.benjaminborbe.storage.api;

import java.util.Collection;

public interface StorageRow {

	StorageValue getKey();

	StorageValue getValue(StorageValue columnName);

	Collection<StorageValue> getColumnNames();
}

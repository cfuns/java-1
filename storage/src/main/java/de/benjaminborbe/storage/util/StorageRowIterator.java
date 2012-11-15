package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageException;

public interface StorageRowIterator {

	boolean hasNext() throws StorageException;

	StorageRow next() throws StorageException;
}

package de.benjaminborbe.storage.api;

public interface StorageColumnIterator {

	boolean hasNext() throws StorageException;

	StorageColumn next() throws StorageException;
}

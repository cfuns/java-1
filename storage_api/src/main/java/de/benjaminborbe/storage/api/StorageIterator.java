package de.benjaminborbe.storage.api;

public interface StorageIterator {

	boolean hasNext() throws StorageException;

	StorageValue next() throws StorageException;

}

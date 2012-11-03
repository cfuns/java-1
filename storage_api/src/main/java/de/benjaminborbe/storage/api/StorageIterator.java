package de.benjaminborbe.storage.api;

public interface StorageIterator {

	boolean hasNext() throws StorageException;

	byte[] next() throws StorageException;
}

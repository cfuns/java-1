package de.benjaminborbe.storage.api;


public interface StorageRowIterator {

	boolean hasNext() throws StorageException;

	StorageRow next() throws StorageException;
}

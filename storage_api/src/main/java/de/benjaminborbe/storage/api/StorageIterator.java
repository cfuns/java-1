package de.benjaminborbe.storage.api;

public interface StorageIterator {

	boolean hasNext() throws StorageException;

	byte[] nextByte() throws StorageException;

	String nextString() throws StorageException;
}

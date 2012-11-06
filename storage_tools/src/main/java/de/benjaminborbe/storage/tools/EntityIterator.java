package de.benjaminborbe.storage.tools;

public interface EntityIterator<T> {

	boolean hasNext() throws EntityIteratorException;

	T next() throws EntityIteratorException;
}

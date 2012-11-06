package de.benjaminborbe.storage.tools;

public interface IdentifierIterator<T> {

	boolean hasNext() throws IdentifierIteratorException;

	T next() throws IdentifierIteratorException;
}

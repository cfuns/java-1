package de.benjaminborbe.storage.tools;

public interface EntityIterator<T> {

	boolean hasNext();

	T next();
}

package de.benjaminborbe.storage.tools;

import java.util.Iterator;

public class EntityIteratorImpl<E> implements EntityIterator<E> {

	private final Iterator<E> iterator;

	public EntityIteratorImpl(final Iterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public E next() {
		return iterator.next();
	}
}

package de.benjaminborbe.storage.tools;

import java.util.NoSuchElementException;

import com.google.common.base.Predicate;

public class EntityIteratorFilter<E> implements EntityIterator<E> {

	private final EntityIterator<E> entityIterator;

	private final Predicate<E> predicate;

	private E next;

	public EntityIteratorFilter(final EntityIterator<E> entityIterator, final Predicate<E> predicate) {
		this.entityIterator = entityIterator;
		this.predicate = predicate;
	}

	@Override
	public boolean hasNext() throws EntityIteratorException {
		if (next != null) {
			return true;
		}
		while (entityIterator.hasNext()) {
			final E e = entityIterator.next();
			if (predicate.apply(e)) {
				next = e;
				return true;
			}
		}
		return false;
	}

	@Override
	public E next() throws EntityIteratorException {
		if (hasNext()) {
			final E result = next;
			next = null;
			return result;
		}
		else {
			throw new NoSuchElementException();
		}
	}
}

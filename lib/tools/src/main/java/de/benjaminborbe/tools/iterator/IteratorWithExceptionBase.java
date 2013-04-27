package de.benjaminborbe.tools.iterator;

import de.benjaminborbe.api.IteratorWithException;

import java.util.NoSuchElementException;

public abstract class IteratorWithExceptionBase<T, E extends Exception> implements IteratorWithException<T, E> {

	private T next = null;

	@Override
	public boolean hasNext() throws E {
		if (next == null) {
			next = buildNext();
		}
		return next != null;
	}

	protected abstract T buildNext() throws E;

	@Override
	public T next() throws E {
		if (hasNext()) {
			final T result = next;
			next = null;
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}

}

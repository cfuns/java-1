package de.benjaminborbe.tools.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.benjaminborbe.api.IteratorWithoutException;

public abstract class IteratorWithoutExceptionBase<T> implements IteratorWithoutException<T> {

	private T next = null;

	@Override
	public boolean hasNext() {
		if (next == null) {
			next = buildNext();
		}
		return next != null;
	}

	protected abstract T buildNext();

	@Override
	public T next() {
		if (hasNext()) {
			final T result = next;
			next = null;
			return result;
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}

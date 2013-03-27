package de.benjaminborbe.tools.iterator;

import de.benjaminborbe.api.IteratorWithException;

public class IteratorCurrent<T, E extends Exception> implements IteratorWithException<T, E> {

	private final IteratorWithException<T, E> iterator;

	private T current;

	public IteratorCurrent(final IteratorWithException<T, E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() throws E {
		return iterator.hasNext();
	}

	@Override
	public T next() throws E {
		return current = iterator.next();
	}

	public T getCurrent() {
		return current;
	}

	public void setCurrent(final T current) {
		this.current = current;
	}

}

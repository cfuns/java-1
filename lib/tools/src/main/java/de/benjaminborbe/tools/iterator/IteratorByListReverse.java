package de.benjaminborbe.tools.iterator;

import de.benjaminborbe.api.IteratorWithException;

import java.util.Arrays;
import java.util.List;

public class IteratorByListReverse<T, E extends Exception> implements IteratorWithException<T, E> {

	private int pos;

	private final List<T> values;

	public IteratorByListReverse(final T... values) {
		this(Arrays.asList(values));
	}

	public IteratorByListReverse(final List<T> values) {
		this.values = values;
		pos = values.size() - 1;
	}

	@Override
	public boolean hasNext() throws E {
		return pos >= 0;
	}

	@Override
	public T next() throws E {
		final T result = values.get(pos);
		pos--;
		return result;
	}

}

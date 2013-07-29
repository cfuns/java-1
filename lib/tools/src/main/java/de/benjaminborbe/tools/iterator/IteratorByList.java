package de.benjaminborbe.tools.iterator;

import de.benjaminborbe.api.IteratorWithException;

import java.util.Arrays;
import java.util.List;

public class IteratorByList<T, E extends Exception> implements IteratorWithException<T, E> {

	private int pos = 0;

	private final List<T> values;

	public IteratorByList(final T... values) {
		this(Arrays.asList(values));
	}

	public IteratorByList(final List<T> values) {
		this.values = values;
	}

	@Override
	public boolean hasNext() throws E {
		return pos < values.size();
	}

	@Override
	public T next() throws E {
		final T result = values.get(pos);
		pos++;
		return result;
	}

}

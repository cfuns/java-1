package de.benjaminborbe.storage.tools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EntityIteratorList<T> implements EntityIterator<T> {

	private final Iterator<T> iterator;

	public EntityIteratorList(final T... values) {
		this(Arrays.asList(values));
	}

	public EntityIteratorList(final List<T> list) {
		this.iterator = list.iterator();
	}

	@Override
	public boolean hasNext() throws EntityIteratorException {
		return iterator.hasNext();
	}

	@Override
	public T next() throws EntityIteratorException {
		return iterator.next();
	}
}

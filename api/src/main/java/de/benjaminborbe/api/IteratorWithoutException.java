package de.benjaminborbe.api;

import java.util.Iterator;

public interface IteratorWithoutException<I> extends IteratorWithException<I, Exception>, Iterator<I>, Iterable<I> {

	@Override
	boolean hasNext();

	@Override
	I next();
}

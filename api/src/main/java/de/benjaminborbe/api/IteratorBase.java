package de.benjaminborbe.api;

public interface IteratorBase<I, E extends Exception> {

	boolean hasNext() throws E;

	I next() throws E;
}

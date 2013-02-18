package de.benjaminborbe.api;

public interface IteratorWithException<I, E extends Exception> {

	boolean hasNext() throws E;

	I next() throws E;
}

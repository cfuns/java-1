package de.benjaminborbe.tools.mock;

import java.util.Enumeration;

public class EnumerationEmpty<T> implements Enumeration<T> {

	@Override
	public boolean hasMoreElements() {
		return false;
	}

	@Override
	public T nextElement() {
		return null;
	}

}

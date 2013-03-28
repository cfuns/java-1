package de.benjaminborbe.tools.jndi;

import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class NamingEnumerationMock<T> implements NamingEnumeration<T> {

	private final Iterator<T> it;

	public NamingEnumerationMock(final Iterator<T> it) {
		super();
		this.it = it;
	}

	public NamingEnumerationMock() {
		super();
		this.it = new ArrayList<T>().iterator();
	}

	public void close() throws NamingException {

	}

	public boolean hasMore() throws NamingException {
		return it.hasNext();
	}

	public T next() throws NamingException {
		return it.next();
	}

	public boolean hasMoreElements() {
		return it.hasNext();
	}

	public T nextElement() {
		return it.next();
	}

}

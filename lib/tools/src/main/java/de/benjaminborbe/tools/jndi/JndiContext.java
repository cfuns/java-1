package de.benjaminborbe.tools.jndi;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public interface JndiContext {

	Object lookup(String name) throws NamingException;

	@SuppressWarnings("rawtypes")
	NamingEnumeration list(final String name) throws NamingException;

}

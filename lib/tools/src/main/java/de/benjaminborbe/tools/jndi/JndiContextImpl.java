package de.benjaminborbe.tools.jndi;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JndiContextImpl implements JndiContext {

	private final InitialContextCache initialContextCache;

	@Inject
	public JndiContextImpl(final InitialContextCache initialContextCache) {
		this.initialContextCache = initialContextCache;

	}

	@Override
	public Object lookup(final String name) throws NamingException {
		final Context envContext = (Context) initialContextCache.lookup("java:/comp/env");
		return envContext.lookup(name);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public NamingEnumeration list(final String name) throws NamingException {
		final Context envContext = (Context) initialContextCache.lookup("java:/comp/env");
		return envContext.list(name);
	}

}

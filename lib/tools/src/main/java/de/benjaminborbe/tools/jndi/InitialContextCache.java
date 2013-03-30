package de.benjaminborbe.tools.jndi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class InitialContextCache {

	private InitialContext initContext;

	private final Map<String, Object> cache = new HashMap<>();

	@Inject
	public InitialContextCache() {
		try {
			initContext = new InitialContext();
		} catch (final NamingException e) {
			initContext = null;
		}
	}

	public Object lookup(final String envName) throws NamingException {
		if (initContext == null) {
			throw new NamingException("initContext null");
		}
		if (!cache.containsKey(envName)) {
			final Object result = initContext.lookup(envName);
			cache.put(envName, result);
		}
		return cache.get(envName);
	}

}

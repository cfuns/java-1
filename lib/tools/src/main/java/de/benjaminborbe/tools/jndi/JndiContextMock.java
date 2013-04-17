package de.benjaminborbe.tools.jndi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class JndiContextMock implements JndiContext {

	private final Map<String, Object> values = new HashMap<>();

	@Inject
	public JndiContextMock() {

	}

	public Object lookup(final String name) throws NamingException {
		if (!values.containsKey(name)) {
			return null;
		}
		return values.get(name);
	}

	public void setValue(final String name, final Object value) {
		values.put(name, value);
	}

	public void clearValues() {
		values.clear();
	}

	@SuppressWarnings("rawtypes")
	public NamingEnumeration list(final String name) throws NamingException {
		return new NamingEnumerationMock();
	}
}

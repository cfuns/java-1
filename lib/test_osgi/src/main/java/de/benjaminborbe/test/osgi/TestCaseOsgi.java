package de.benjaminborbe.test.osgi;

import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;

public class TestCaseOsgi extends OSGiTestCase {

	@SuppressWarnings("unchecked")
	protected <T> T getService(final Class<T> clazz) {
		final Object serviceObject = getServiceObject(clazz.getName(), null);
		return (T) serviceObject;
	}
}

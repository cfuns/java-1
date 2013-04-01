package de.benjaminborbe.authentication.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class AuthenticationTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Authentication Test Suite", bc);
		ots.addTestSuite(AuthenticationIntegrationTest.class);
		return ots;
	}
}

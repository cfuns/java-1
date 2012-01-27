package de.benjaminborbe.authorization.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class AuthorizationTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Authorization Test Suite", bc);
		ots.addTestSuite(AuthorizationTest.class);
		return ots;
	}
}

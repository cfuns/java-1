package de.benjaminborbe.navigation.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class NavigationTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Navigation Test Suite", bc);
		ots.addTestSuite(NavigationIntegrationTest.class);
		return ots;
	}
}

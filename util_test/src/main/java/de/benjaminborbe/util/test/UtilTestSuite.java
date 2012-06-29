package de.benjaminborbe.util.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class UtilTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Util Test Suite", bc);
		ots.addTestSuite(UtilIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.loggly.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class LogglyTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Loggly Test Suite", bc);
		ots.addTestSuite(LogglyIntegrationTest.class);
		return ots;
	}
}

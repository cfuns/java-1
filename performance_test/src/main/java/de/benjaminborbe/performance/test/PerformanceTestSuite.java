package de.benjaminborbe.performance.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class PerformanceTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Performance Test Suite", bc);
		ots.addTestSuite(PerformanceIntegrationTest.class);
		return ots;
	}
}

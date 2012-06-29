package de.benjaminborbe.monitoring.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class MonitoringTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Storage Test Suite", bc);
		ots.addTestSuite(MonitoringIntegrationTest.class);
		return ots;
	}
}

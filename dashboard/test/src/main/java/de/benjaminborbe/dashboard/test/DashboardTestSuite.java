package de.benjaminborbe.dashboard.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class DashboardTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Dashboard Test Suite", bc);
		ots.addTestSuite(DashboardIntegrationTest.class);
		return ots;
	}
}

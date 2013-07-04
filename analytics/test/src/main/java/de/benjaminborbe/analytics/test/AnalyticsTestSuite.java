package de.benjaminborbe.analytics.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class AnalyticsTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Analytics Test Suite", bc);
		ots.addTestSuite(AnalyticsIntegrationTest.class);
		return ots;
	}
}

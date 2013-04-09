package de.benjaminborbe.lunch.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class LunchTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Lunch Test Suite", bc);
		ots.addTestSuite(LunchIntegrationTest.class);
		return ots;
	}
}

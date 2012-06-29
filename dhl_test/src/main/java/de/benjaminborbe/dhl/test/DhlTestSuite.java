package de.benjaminborbe.dhl.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class DhlTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Dhl Test Suite", bc);
		ots.addTestSuite(DhlIntegrationTest.class);
		return ots;
	}
}

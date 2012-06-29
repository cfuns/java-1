package de.benjaminborbe.systemstatus.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class SystemstatusTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Systemstatus Test Suite", bc);
		ots.addTestSuite(SystemstatusIntegrationTest.class);
		return ots;
	}
}

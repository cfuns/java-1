package de.benjaminborbe.kiosk.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class KioskTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Kiosk Test Suite", bc);
		ots.addTestSuite(KioskIntegrationTest.class);
		return ots;
	}
}

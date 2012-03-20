package de.benjaminborbe.wow.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WowTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Wow Test Suite", bc);
		ots.addTestSuite(WowTest.class);
		return ots;
	}
}

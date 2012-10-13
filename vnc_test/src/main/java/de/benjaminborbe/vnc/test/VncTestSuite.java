package de.benjaminborbe.vnc.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class VncTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Vnc Test Suite", bc);
		ots.addTestSuite(VncIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.virt.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class VirtTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Virt Test Suite", bc);
		ots.addTestSuite(VirtIntegrationTest.class);
		return ots;
	}
}

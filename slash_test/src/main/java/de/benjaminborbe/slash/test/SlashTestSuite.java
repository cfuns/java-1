package de.benjaminborbe.slash.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class SlashTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Slash Test Suite", bc);
		ots.addTestSuite(SlashIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.googlesearch.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class GooglesearchTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Googlesearch Test Suite", bc);
		ots.addTestSuite(GooglesearchIntegrationTest.class);
		return ots;
	}
}

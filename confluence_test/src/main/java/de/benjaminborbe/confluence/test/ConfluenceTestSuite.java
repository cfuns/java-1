package de.benjaminborbe.confluence.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ConfluenceTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Confluence Test Suite", bc);
		ots.addTestSuite(ConfluenceIntegrationTest.class);
		return ots;
	}
}

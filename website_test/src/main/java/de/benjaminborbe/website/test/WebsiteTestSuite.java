package de.benjaminborbe.website.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WebsiteTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Website Test Suite", bc);
		ots.addTestSuite(WebsiteTest.class);
		return ots;
	}
}

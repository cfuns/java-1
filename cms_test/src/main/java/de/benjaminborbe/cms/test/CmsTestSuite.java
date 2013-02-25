package de.benjaminborbe.cms.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class CmsTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Cms Test Suite", bc);
		ots.addTestSuite(CmsIntegrationTest.class);
		return ots;
	}
}

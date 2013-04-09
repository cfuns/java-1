package de.benjaminborbe.cache.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class CacheTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Cache Test Suite", bc);
		ots.addTestSuite(CacheIntegrationTest.class);
		return ots;
	}
}

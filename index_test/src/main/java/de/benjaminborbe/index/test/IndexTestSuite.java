package de.benjaminborbe.index.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class IndexTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Index Test Suite", bc);
		ots.addTestSuite(IndexIntegrationTest.class);
		return ots;
	}
}

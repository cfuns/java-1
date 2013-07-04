package de.benjaminborbe.shortener.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ShortenerTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Shortener Test Suite", bc);
		ots.addTestSuite(ShortenerIntegrationTest.class);
		return ots;
	}
}

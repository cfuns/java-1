package de.benjaminborbe.httpdownloader.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class HttpdownloaderTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("HttpdownloaderAction Test Suite", bc);
		ots.addTestSuite(HttpdownloaderIntegrationTest.class);
		return ots;
	}
}

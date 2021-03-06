package de.benjaminborbe.websearch.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WebsearchTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Websearch Test Suite", bc);
		ots.addTestSuite(WebsearchIntegrationTest.class);
		return ots;
	}
}

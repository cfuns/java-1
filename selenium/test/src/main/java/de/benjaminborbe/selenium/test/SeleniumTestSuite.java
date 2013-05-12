package de.benjaminborbe.selenium.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class SeleniumTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Selenium Test Suite", bc);
		ots.addTestSuite(SeleniumIntegrationTest.class);
		return ots;
	}
}

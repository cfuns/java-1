package de.benjaminborbe.portfolio.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class PortfolioTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Portfolio Test Suite", bc);
		ots.addTestSuite(PortfolioIntegrationTest.class);
		return ots;
	}
}

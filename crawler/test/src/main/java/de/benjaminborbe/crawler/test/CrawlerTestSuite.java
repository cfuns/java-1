package de.benjaminborbe.crawler.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class CrawlerTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Crawler Test Suite", bc);
		ots.addTestSuite(CrawlerIntegrationTest.class);
		return ots;
	}
}

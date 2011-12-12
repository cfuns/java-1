package de.benjaminborbe.search.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class SearchTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Search Test Suite", bc);
		ots.addTestSuite(SearchTest.class);
		return ots;
	}
}

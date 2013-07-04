package de.benjaminborbe.wiki.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WikiTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Wiki Test Suite", bc);
		ots.addTestSuite(WikiIntegrationTest.class);
		return ots;
	}
}

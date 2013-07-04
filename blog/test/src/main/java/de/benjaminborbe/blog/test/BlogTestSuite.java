package de.benjaminborbe.blog.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class BlogTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Blog Test Suite", bc);
		ots.addTestSuite(BlogIntegrationTest.class);
		return ots;
	}
}

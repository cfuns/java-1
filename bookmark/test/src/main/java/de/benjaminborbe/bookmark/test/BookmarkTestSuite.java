package de.benjaminborbe.bookmark.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class BookmarkTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Bookmark Test Suite", bc);
		ots.addTestSuite(BookmarkIntegrationTest.class);
		return ots;
	}
}

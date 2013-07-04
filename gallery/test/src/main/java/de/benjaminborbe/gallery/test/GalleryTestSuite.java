package de.benjaminborbe.gallery.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class GalleryTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Gallery Test Suite", bc);
		ots.addTestSuite(GalleryIntegrationTest.class);
		return ots;
	}
}

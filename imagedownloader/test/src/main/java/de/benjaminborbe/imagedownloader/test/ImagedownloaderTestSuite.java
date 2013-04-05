package de.benjaminborbe.imagedownloader.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ImagedownloaderTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Imagedownloader Test Suite", bc);
		ots.addTestSuite(ImagedownloaderIntegrationTest.class);
		return ots;
	}
}

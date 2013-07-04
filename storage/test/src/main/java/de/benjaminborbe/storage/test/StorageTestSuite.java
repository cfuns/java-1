package de.benjaminborbe.storage.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class StorageTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Storage Test Suite", bc);
		ots.addTestSuite(StorageIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.storage.memory.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class StorageMemoryTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Storage Test Suite", bc);
		ots.addTestSuite(StorageMemoryIntegrationTest.class);
		return ots;
	}
}

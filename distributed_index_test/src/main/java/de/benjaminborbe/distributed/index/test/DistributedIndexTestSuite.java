package de.benjaminborbe.distributed.index.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class DistributedIndexTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("DistributedIndex Test Suite", bc);
		ots.addTestSuite(DistributedIndexIntegrationTest.class);
		return ots;
	}
}

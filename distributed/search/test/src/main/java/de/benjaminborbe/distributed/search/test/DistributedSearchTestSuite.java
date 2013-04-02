package de.benjaminborbe.distributed.search.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class DistributedSearchTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("DistributedSearch Test Suite", bc);
		ots.addTestSuite(DistributedSearchIntegrationTest.class);
		return ots;
	}
}

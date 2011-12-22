package de.benjaminborbe.sample.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class SampleTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Sample Test Suite", bc);
		ots.addTestSuite(SampleTest.class);
		return ots;
	}
}

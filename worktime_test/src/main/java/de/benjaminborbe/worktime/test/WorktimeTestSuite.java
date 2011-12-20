package de.benjaminborbe.worktime.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WorktimeTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Worktime Test Suite", bc);
		ots.addTestSuite(WorktimeTest.class);
		return ots;
	}
}

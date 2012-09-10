package de.benjaminborbe.task.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class TaskTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Task Test Suite", bc);
		ots.addTestSuite(TaskIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.notification.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class NotificationTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Notification Test Suite", bc);
		ots.addTestSuite(NotificationIntegrationTest.class);
		return ots;
	}
}

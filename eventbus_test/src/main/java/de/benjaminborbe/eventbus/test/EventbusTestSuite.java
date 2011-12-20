package de.benjaminborbe.eventbus.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class EventbusTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Eventbus Test Suite", bc);
		ots.addTestSuite(EventbusTest.class);
		return ots;
	}
}

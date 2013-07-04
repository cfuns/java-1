package de.benjaminborbe.calendar.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class CalendarTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Calendar Test Suite", bc);
		ots.addTestSuite(CalendarIntegrationTest.class);
		return ots;
	}
}

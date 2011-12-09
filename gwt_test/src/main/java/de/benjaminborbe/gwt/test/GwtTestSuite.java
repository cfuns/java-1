package de.benjaminborbe.gwt.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class GwtTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Gwt Test Suite", bc);
		ots.addTestSuite(GwtTest.class);
		return ots;
	}
}

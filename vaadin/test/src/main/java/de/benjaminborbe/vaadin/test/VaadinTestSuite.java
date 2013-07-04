package de.benjaminborbe.vaadin.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class VaadinTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Vaadin Test Suite", bc);
		ots.addTestSuite(VaadinIntegrationTest.class);
		return ots;
	}
}

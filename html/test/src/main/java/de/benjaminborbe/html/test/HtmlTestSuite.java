package de.benjaminborbe.html.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class HtmlTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Html Test Suite", bc);
		ots.addTestSuite(HtmlIntegrationTest.class);
		return ots;
	}
}

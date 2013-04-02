package de.benjaminborbe.proxy.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ProxyTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Proxy Test Suite", bc);
		ots.addTestSuite(ProxyIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.dns.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class DnsTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Dns Test Suite", bc);
		ots.addTestSuite(DnsIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.message.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class MessageTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Messageservice Test Suite", bc);
		ots.addTestSuite(MessageIntegrationTest.class);
		return ots;
	}
}

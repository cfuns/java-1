package de.benjaminborbe.messageservice.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class MessageserviceTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Messageservice Test Suite", bc);
		ots.addTestSuite(MessageserviceTest.class);
		return ots;
	}
}

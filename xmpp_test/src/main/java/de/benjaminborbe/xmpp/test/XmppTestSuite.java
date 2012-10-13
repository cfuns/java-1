package de.benjaminborbe.xmpp.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class XmppTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Xmpp Test Suite", bc);
		ots.addTestSuite(XmppIntegrationTest.class);
		return ots;
	}
}

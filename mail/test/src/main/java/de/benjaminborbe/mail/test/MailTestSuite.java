package de.benjaminborbe.mail.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class MailTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Mail Test Suite", bc);
		ots.addTestSuite(MailIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.microblog.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class MicroblogTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Microblog Test Suite", bc);
		ots.addTestSuite(MicroblogTest.class);
		return ots;
	}
}

package de.benjaminborbe.translate.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class TranslateTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Translate Test Suite", bc);
		ots.addTestSuite(TranslateIntegrationTest.class);
		return ots;
	}
}

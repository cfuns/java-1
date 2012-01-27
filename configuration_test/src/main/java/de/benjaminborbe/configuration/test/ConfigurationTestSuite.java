package de.benjaminborbe.configuration.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ConfigurationTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Configuration Test Suite", bc);
		ots.addTestSuite(ConfigurationTest.class);
		return ots;
	}
}

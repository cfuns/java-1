package de.benjaminborbe.scala.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ScalaTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Scala Test Suite", bc);
		ots.addTestSuite(ScalaIntegrationTest.class);
		return ots;
	}
}

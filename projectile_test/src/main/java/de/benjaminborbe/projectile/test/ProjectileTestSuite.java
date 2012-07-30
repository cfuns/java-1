package de.benjaminborbe.projectile.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class ProjectileTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Projectile Test Suite", bc);
		ots.addTestSuite(ProjectileIntegrationTest.class);
		return ots;
	}
}

package de.benjaminborbe.poker.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class PokerTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Poker Test Suite", bc);
		ots.addTestSuite(PokerServicesIntegrationTest.class);
		ots.addTestSuite(PokerApiDisabledIntegrationTest.class);
		ots.addTestSuite(PokerStatusIntegrationTest.class);
		return ots;
	}
}

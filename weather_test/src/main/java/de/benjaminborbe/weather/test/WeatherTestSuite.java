package de.benjaminborbe.weather.test;

import junit.framework.Test;

import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class WeatherTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Weather Test Suite", bc);
		ots.addTestSuite(WeatherTest.class);
		return ots;
	}
}

package de.benjaminborbe.note.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class NoteTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("Note Test Suite", bc);
		ots.addTestSuite(NoteIntegrationTest.class);
		return ots;
	}
}

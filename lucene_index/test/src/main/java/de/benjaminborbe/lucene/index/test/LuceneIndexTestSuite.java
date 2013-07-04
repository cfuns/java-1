package de.benjaminborbe.lucene.index.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

public class LuceneIndexTestSuite {

	public static Test suite(final BundleContext bc) {
		final OSGiTestSuite ots = new OSGiTestSuite("LuceneIndex Test Suite", bc);
		ots.addTestSuite(LuceneIndexIntegrationTest.class);
		return ots;
	}
}

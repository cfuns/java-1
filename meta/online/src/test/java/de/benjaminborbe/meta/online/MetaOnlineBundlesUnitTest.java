package de.benjaminborbe.meta.online;

import de.benjaminborbe.meta.util.BundleResolver;
import de.benjaminborbe.meta.util.BundleResolverImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MetaOnlineBundlesUnitTest {

	@Test
	public void testBundles() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final BundleResolver bundleResolver = new BundleResolverImpl(logger);
		final List<String> names = bundleResolver.getBundleSymbolicNames();
		assertNotNull(names);
		assertTrue(names.size() > 0);
		final Set<String> namesUnique = new HashSet<>(names);
		assertEquals("dupplicate bundle!", names.size(), namesUnique.size());
	}
}

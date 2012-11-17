package de.benjaminborbe.meta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class BundleResolverImplUnitTest {

	@Test
	public void testGet() throws Exception {
		final BundleResolverImpl b = new BundleResolverImpl();
		final List<String> list = b.getBundleSymbolicNames();
		assertNotNull(list);
		assertEquals(3, list.size());
	}

}

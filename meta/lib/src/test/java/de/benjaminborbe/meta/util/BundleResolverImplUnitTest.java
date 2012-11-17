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

	@Test
	public void testParse() {
		final BundleResolverImpl b = new BundleResolverImpl();
		assertEquals(0, b.parseValue(null).size());
		assertEquals(0, b.parseValue("").size());
		assertEquals(1, b.parseValue("  a  ").size());
		assertEquals(2, b.parseValue("a, b ").size());
	}

}

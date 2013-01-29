package de.benjaminborbe.meta.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class BundleResolverImplUnitTest {

	@Test
	public void testGet() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final BundleResolverImpl b = new BundleResolverImpl(logger);
		final List<String> list = b.getBundleSymbolicNames();
		assertNotNull(list);
		assertEquals(3, list.size());

		Collections.sort(list);

		assertThat(list.get(0), is("de.benjaminborbe.authentication.gui"));
		assertThat(list.get(1), is("de.benjaminborbe.authorization.gui"));
		assertThat(list.get(2), is("de.benjaminborbe.portfolio.gui"));

	}

	@Test
	public void testParse() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final BundleResolverImpl b = new BundleResolverImpl(logger);
		assertEquals(0, b.parseValue(null).size());
		assertEquals(0, b.parseValue("").size());
		assertEquals(1, b.parseValue("  a  ").size());
		assertEquals(2, b.parseValue("a, b ").size());
	}

}

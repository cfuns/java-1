package de.benjaminborbe.portfolio.gui.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PortfolioGuiCacheUtilUnitTest {

	@Test
	public void testCacheable() throws Exception {
		final PortfolioGuiCacheUtil util = new PortfolioGuiCacheUtil();
		assertFalse(util.isCacheable("/bb", "/bb/task"));
		assertFalse(util.isCacheable("/bb", "/bb/test.jpg"));
		assertTrue(util.isCacheable("/bb", "/bb/portfolio/test.jpg"));
		assertTrue(util.isCacheable("/bb", "/bb/portfolio/"));
	}

}

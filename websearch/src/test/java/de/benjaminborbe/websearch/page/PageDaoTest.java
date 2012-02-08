package de.benjaminborbe.websearch.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

public class PageDaoTest {

	@Test
	public void testFindSubPages() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Provider<PageBean> provider = new Provider<PageBean>() {

			@Override
			public PageBean get() {
				return new PageBean();
			}
		};
		final PageDao pageDao = new PageDaoImpl(logger, provider);
		final URL url1 = new URL("http://www.test.de/");
		final URL url2 = new URL("http://www.test.de/admin/");
		final URL url3 = new URL("http://www.test.de/admin/list");
		pageDao.findOrCreate(url1);
		pageDao.findOrCreate(url2);
		pageDao.findOrCreate(url3);

		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDao.findSubPages(url1));
			assertEquals(3, subPages.size());
			assertTrue(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDao.findSubPages(url2));
			assertEquals(2, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDao.findSubPages(url3));
			assertEquals(1, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertFalse(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
	}

	protected boolean containsUrl(final Set<PageBean> pages, final URL url) {
		for (final PageBean page : pages) {
			if (url.equals(page.getId())) {
				return true;
			}
		}
		return false;
	}
}

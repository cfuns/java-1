package de.benjaminborbe.websearch.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.storage.tools.EntityIteratorImpl;
import de.benjaminborbe.websearch.api.PageIdentifier;

public class PageDaoSubPagesActionUnitTest {

	@Test
	public void testFindSubPages() throws Exception {
		final PageDaoSubPagesAction pageDaoSubPagesAction = new PageDaoSubPagesAction();
		final URL url1 = new URL("http://www.test.de/");
		final PageBean page1 = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page1.getUrl()).andReturn(url1).anyTimes();
		EasyMock.expect(page1.getId()).andReturn(new PageIdentifier(url1)).anyTimes();
		EasyMock.replay(page1);

		final URL url2 = new URL("http://www.test.de/admin/");
		final PageBean page2 = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page2.getUrl()).andReturn(url2).anyTimes();
		EasyMock.expect(page2.getId()).andReturn(new PageIdentifier(url2)).anyTimes();
		EasyMock.replay(page2);

		final URL url3 = new URL("http://www.test.de/admin/list");
		final PageBean page3 = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page3.getUrl()).andReturn(url3).anyTimes();
		EasyMock.expect(page3.getId()).andReturn(new PageIdentifier(url3)).anyTimes();
		EasyMock.replay(page3);

		final Set<PageBean> allPages = new HashSet<PageBean>();
		allPages.add(page1);
		allPages.add(page2);
		allPages.add(page3);
		assertEquals(3, allPages.size());

		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDaoSubPagesAction.findSubPages(url1, new EntityIteratorImpl<PageBean>(allPages.iterator())));
			assertEquals(3, subPages.size());
			assertTrue(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDaoSubPagesAction.findSubPages(url2, new EntityIteratorImpl<PageBean>(allPages.iterator())));
			assertEquals(2, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<PageBean> subPages = new HashSet<PageBean>(pageDaoSubPagesAction.findSubPages(url3, new EntityIteratorImpl<PageBean>(allPages.iterator())));
			assertEquals(1, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertFalse(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
	}

	protected boolean containsUrl(final Set<PageBean> pages, final URL url) {
		for (final PageBean page : pages) {
			if (url.equals(page.getUrl())) {
				return true;
			}
		}
		return false;
	}
}

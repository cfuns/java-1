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

public class WebsearchPageDaoSubPagesActionUnitTest {

	@Test
	public void testFindSubPages() throws Exception {
		final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction = new WebsearchPageDaoSubPagesAction();
		final URL url1 = new URL("http://www.test.de/");
		final WebsearchPageBean page1 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page1.getUrl()).andReturn(url1).anyTimes();
		EasyMock.expect(page1.getId()).andReturn(new PageIdentifier(url1)).anyTimes();
		EasyMock.replay(page1);

		final URL url2 = new URL("http://www.test.de/admin/");
		final WebsearchPageBean page2 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page2.getUrl()).andReturn(url2).anyTimes();
		EasyMock.expect(page2.getId()).andReturn(new PageIdentifier(url2)).anyTimes();
		EasyMock.replay(page2);

		final URL url3 = new URL("http://www.test.de/admin/list");
		final WebsearchPageBean page3 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page3.getUrl()).andReturn(url3).anyTimes();
		EasyMock.expect(page3.getId()).andReturn(new PageIdentifier(url3)).anyTimes();
		EasyMock.replay(page3);

		final Set<WebsearchPageBean> allPages = new HashSet<WebsearchPageBean>();
		allPages.add(page1);
		allPages.add(page2);
		allPages.add(page3);
		assertEquals(3, allPages.size());

		{
			final Set<WebsearchPageBean> subPages = new HashSet<WebsearchPageBean>(pageDaoSubPagesAction.findSubPages(url1, new EntityIteratorImpl<WebsearchPageBean>(allPages.iterator())));
			assertEquals(3, subPages.size());
			assertTrue(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<WebsearchPageBean> subPages = new HashSet<WebsearchPageBean>(pageDaoSubPagesAction.findSubPages(url2, new EntityIteratorImpl<WebsearchPageBean>(allPages.iterator())));
			assertEquals(2, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<WebsearchPageBean> subPages = new HashSet<WebsearchPageBean>(pageDaoSubPagesAction.findSubPages(url3, new EntityIteratorImpl<WebsearchPageBean>(allPages.iterator())));
			assertEquals(1, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertFalse(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
	}

	protected boolean containsUrl(final Set<WebsearchPageBean> pages, final URL url) {
		for (final WebsearchPageBean page : pages) {
			if (url.equals(page.getUrl())) {
				return true;
			}
		}
		return false;
	}
}
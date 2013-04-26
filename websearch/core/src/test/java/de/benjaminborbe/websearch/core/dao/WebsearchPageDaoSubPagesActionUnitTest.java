package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.storage.tools.EntityIteratorImpl;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import org.easymock.EasyMock;
import org.junit.Test;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebsearchPageDaoSubPagesActionUnitTest {

	@Test
	public void testFindSubPages() throws Exception {
		final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction = new WebsearchPageDaoSubPagesAction();
		final URL url1 = new URL("http://www.example.com/");
		final WebsearchPageBean page1 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page1.getUrl()).andReturn(url1).anyTimes();
		EasyMock.expect(page1.getId()).andReturn(new WebsearchPageIdentifier(url1.toExternalForm())).anyTimes();
		EasyMock.replay(page1);

		final URL url2 = new URL("http://www.example.com/admin/");
		final WebsearchPageBean page2 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page2.getUrl()).andReturn(url2).anyTimes();
		EasyMock.expect(page2.getId()).andReturn(new WebsearchPageIdentifier(url2.toExternalForm())).anyTimes();
		EasyMock.replay(page2);

		final URL url3 = new URL("http://www.example.com/admin/list");
		final WebsearchPageBean page3 = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page3.getUrl()).andReturn(url3).anyTimes();
		EasyMock.expect(page3.getId()).andReturn(new WebsearchPageIdentifier(url3.toExternalForm())).anyTimes();
		EasyMock.replay(page3);

		final Set<WebsearchPageBean> allPages = new HashSet<>();
		allPages.add(page1);
		allPages.add(page2);
		allPages.add(page3);
		assertEquals(3, allPages.size());

		{
			final Set<WebsearchPageBean> subPages = new HashSet<>(pageDaoSubPagesAction.findSubPages(url1.toExternalForm(), new EntityIteratorImpl<>(allPages.iterator())));
			assertEquals(3, subPages.size());
			assertTrue(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<WebsearchPageBean> subPages = new HashSet<>(pageDaoSubPagesAction.findSubPages(url2.toExternalForm(), new EntityIteratorImpl<>(allPages.iterator())));
			assertEquals(2, subPages.size());
			assertFalse(containsUrl(subPages, url1));
			assertTrue(containsUrl(subPages, url2));
			assertTrue(containsUrl(subPages, url3));
		}
		{
			final Set<WebsearchPageBean> subPages = new HashSet<>(pageDaoSubPagesAction.findSubPages(url3.toExternalForm(), new EntityIteratorImpl<>(allPages.iterator())));
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

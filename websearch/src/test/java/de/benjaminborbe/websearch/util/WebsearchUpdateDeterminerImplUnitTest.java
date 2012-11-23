package de.benjaminborbe.websearch.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.storage.tools.EntityIteratorImpl;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.page.WebsearchPageBean;

public class WebsearchUpdateDeterminerImplUnitTest {

	@Test
	public void testIsSubPageNoConfigurations() throws Exception {
		final WebsearchUpdateDeterminerImpl updateDeterminerImpl = new WebsearchUpdateDeterminerImpl(null, null, null, null);

		final WebsearchPageBean page = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.replay(page);

		final Collection<WebsearchConfigurationBean> configurations = new ArrayList<WebsearchConfigurationBean>();

		assertFalse(updateDeterminerImpl.isSubPage(page, new EntityIteratorImpl<WebsearchConfigurationBean>(configurations.iterator())));
	}

	@Test
	public void testIsSubPageExactMatch() throws Exception {
		final WebsearchUpdateDeterminerImpl updateDeterminerImpl = new WebsearchUpdateDeterminerImpl(null, null, null, null);

		final WebsearchPageBean page = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.replay(page);

		final WebsearchConfigurationBean configurationBean = EasyMock.createMock(WebsearchConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(new ArrayList<String>());
		EasyMock.replay(configurationBean);
		final Collection<WebsearchConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertTrue(updateDeterminerImpl.isSubPage(page, new EntityIteratorImpl<WebsearchConfigurationBean>(configurations.iterator())));
	}

	@Test
	public void testIsSubPage() throws Exception {
		final WebsearchUpdateDeterminerImpl updateDeterminerImpl = new WebsearchUpdateDeterminerImpl(null, null, null, null);

		final WebsearchPageBean page = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/test"));
		EasyMock.replay(page);

		final WebsearchConfigurationBean configurationBean = EasyMock.createMock(WebsearchConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(new ArrayList<String>());
		EasyMock.replay(configurationBean);
		final Collection<WebsearchConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertTrue(updateDeterminerImpl.isSubPage(page, new EntityIteratorImpl<WebsearchConfigurationBean>(configurations.iterator())));
	}

	@Test
	public void testIsSubPageExcludes() throws Exception {
		final WebsearchUpdateDeterminerImpl updateDeterminerImpl = new WebsearchUpdateDeterminerImpl(null, null, null, null);

		final WebsearchPageBean page = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/delete"));
		EasyMock.replay(page);

		final WebsearchConfigurationBean configurationBean = EasyMock.createMock(WebsearchConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(Arrays.asList("delete"));
		EasyMock.replay(configurationBean);
		final Collection<WebsearchConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertFalse(updateDeterminerImpl.isSubPage(page, new EntityIteratorImpl<WebsearchConfigurationBean>(configurations.iterator())));
	}

	@Test
	public void testIsSubPageExcludesQuestionMark() throws Exception {
		final WebsearchUpdateDeterminerImpl updateDeterminerImpl = new WebsearchUpdateDeterminerImpl(null, null, null, null);

		final WebsearchPageBean page = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/?foo=bar"));
		EasyMock.replay(page);

		final WebsearchConfigurationBean configurationBean = EasyMock.createMock(WebsearchConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(Arrays.asList("?"));
		EasyMock.replay(configurationBean);
		final Collection<WebsearchConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertFalse(updateDeterminerImpl.isSubPage(page, new EntityIteratorImpl<WebsearchConfigurationBean>(configurations.iterator())));
	}

	private URL buildUrl(final String url) throws MalformedURLException {
		return new URL(url);
	}
}

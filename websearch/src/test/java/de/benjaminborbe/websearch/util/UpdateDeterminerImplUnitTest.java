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

import de.benjaminborbe.websearch.configuration.ConfigurationBean;
import de.benjaminborbe.websearch.page.PageBean;

public class UpdateDeterminerImplUnitTest {

	@Test
	public void testIsSubPageNoConfigurations() throws Exception {
		final UpdateDeterminerImpl updateDeterminerImpl = new UpdateDeterminerImpl(null, null, null, null);

		final PageBean page = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.replay(page);

		final Collection<ConfigurationBean> configurations = new ArrayList<ConfigurationBean>();

		assertFalse(updateDeterminerImpl.isSubPage(page, configurations));
	}

	@Test
	public void testIsSubPageExactMatch() throws Exception {
		final UpdateDeterminerImpl updateDeterminerImpl = new UpdateDeterminerImpl(null, null, null, null);

		final PageBean page = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.replay(page);

		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(new ArrayList<String>());
		EasyMock.replay(configurationBean);
		final Collection<ConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertTrue(updateDeterminerImpl.isSubPage(page, configurations));
	}

	@Test
	public void testIsSubPage() throws Exception {
		final UpdateDeterminerImpl updateDeterminerImpl = new UpdateDeterminerImpl(null, null, null, null);

		final PageBean page = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/test"));
		EasyMock.replay(page);

		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(new ArrayList<String>());
		EasyMock.replay(configurationBean);
		final Collection<ConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertTrue(updateDeterminerImpl.isSubPage(page, configurations));
	}

	@Test
	public void testIsSubPageExcludes() throws Exception {
		final UpdateDeterminerImpl updateDeterminerImpl = new UpdateDeterminerImpl(null, null, null, null);

		final PageBean page = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/delete"));
		EasyMock.replay(page);

		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(Arrays.asList("delete"));
		EasyMock.replay(configurationBean);
		final Collection<ConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertFalse(updateDeterminerImpl.isSubPage(page, configurations));
	}

	@Test
	public void testIsSubPageExcludesQuestionMark() throws Exception {
		final UpdateDeterminerImpl updateDeterminerImpl = new UpdateDeterminerImpl(null, null, null, null);

		final PageBean page = EasyMock.createMock(PageBean.class);
		EasyMock.expect(page.getUrl()).andReturn(buildUrl("http://www.test.de/?foo=bar"));
		EasyMock.replay(page);

		final ConfigurationBean configurationBean = EasyMock.createMock(ConfigurationBean.class);
		EasyMock.expect(configurationBean.getUrl()).andReturn(buildUrl("http://www.test.de"));
		EasyMock.expect(configurationBean.getExcludes()).andReturn(Arrays.asList("?"));
		EasyMock.replay(configurationBean);
		final Collection<ConfigurationBean> configurations = Arrays.asList(configurationBean);

		assertFalse(updateDeterminerImpl.isSubPage(page, configurations));
	}

	private URL buildUrl(final String url) throws MalformedURLException {
		return new URL(url);
	}
}

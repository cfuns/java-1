package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

public class UrlCheckTest {

	@Test
	public void testContentMatch() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final String urlString = null;
		final String titleMatch = null;
		{
			final String contentMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check1", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkContent("<h1>Content</h1>"));
		}
		{
			final String contentMatch = "bla";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check2", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkContent("<h1>Content</h1>"));
		}
		{
			final String contentMatch = "Content";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check3", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkContent("<h1>Content</h1>"));
		}
	}

	@Test
	public void testTitleMatch() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);
		final String urlString = null;
		final String contentMatch = null;

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		{
			final String titleMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check1", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
		{
			final String titleMatch = "bla";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check2", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
		{
			final String titleMatch = "TestTitle";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check3", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
		{
			final String titleMatch = "TestTitle";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check4", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle("<title>bla TestTitle bla</title>"));
		}
		{
			final String titleMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check5", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle(null));
		}
		{
			final String titleMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check6", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle(""));
		}
		{
			final String titleMatch = "TestTitle";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check5", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkTitle(null));
		}
		{
			final String titleMatch = "TestTitle";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, "Check6", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkTitle(""));
		}
	}

	@Test
	public void testCheckTitle() {
		assertTrue(checkTitle("a", "<title>a</title>"));
		assertTrue(checkTitle("a", "<title> a </title>"));
		assertTrue(checkTitle("a", "<title>\na\n</title>"));
		assertFalse(checkTitle("a", "<title>b</title>"));
		assertTrue(checkTitle("a", "<title>fooa</title>"));
		assertTrue(checkTitle("a", "<title>afoo</title>"));
	}

	protected boolean checkTitle(final String titleMatch, final String content) {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloader httpDownloader = null;
		final HttpDownloadUtil httpDownloadUtil = null;
		final String name = null;
		final String urlString = null;
		final String contentMatch = null;

		final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, httpDownloadUtil, name, urlString, titleMatch, contentMatch);
		return urlCheck.checkTitle(content);
	}
}

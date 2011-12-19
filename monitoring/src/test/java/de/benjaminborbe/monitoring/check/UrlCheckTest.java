package de.benjaminborbe.monitoring.check;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.slf4j.Logger;

import de.benjaminborbe.tools.util.HttpDownloader;

public class UrlCheckTest extends TestCase {

	public void testContentMatch() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);
		final String urlString = null;
		final String titleMatch = null;
		{
			final String contentMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check1", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkContent("<h1>Content</h1>"));
		}
		{
			final String contentMatch = "bla";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check2", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkContent("<h1>Content</h1>"));
		}
		{
			final String contentMatch = "Content";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check3", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkContent("<h1>Content</h1>"));
		}
	}

	public void testTitleMatch() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);
		final String urlString = null;
		final String contentMatch = null;
		{
			final String titleMatch = null;
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check1", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
		{
			final String titleMatch = "bla";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check2", urlString, titleMatch, contentMatch);
			assertFalse(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
		{
			final String titleMatch = "TestTitle";
			final UrlCheck urlCheck = new UrlCheck(logger, httpDownloader, "Check3", urlString, titleMatch, contentMatch);
			assertTrue(urlCheck.checkTitle("<title>TestTitle</title>"));
		}
	}

}

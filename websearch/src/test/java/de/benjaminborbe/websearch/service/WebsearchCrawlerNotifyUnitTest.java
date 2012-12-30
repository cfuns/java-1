package de.benjaminborbe.websearch.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.crawler.api.CrawlerResult;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.page.WebsearchPageDao;

public class WebsearchCrawlerNotifyUnitTest {

	@Test
	public void testExtractTitle() {
		final String title = "Foo Bar Title";
		final String content = "<html><title>" + title + "</title><body></body></html>";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final IndexerService indexerService = EasyMock.createMock(IndexerService.class);
		EasyMock.replay(indexerService);

		final StringUtil stringUtil = EasyMock.createMock(StringUtil.class);
		EasyMock.expect(stringUtil.shorten(title, 80)).andReturn(title);
		EasyMock.replay(stringUtil);

		final WebsearchPageDao pageDao = EasyMock.createMock(WebsearchPageDao.class);
		EasyMock.replay(pageDao);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.filterHtmlTages(title)).andReturn(title);
		EasyMock.replay(htmlUtil);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, null, indexerService, stringUtil, pageDao, htmlUtil);
		assertEquals(title, websearchCrawlerNotify.extractTitle(content));
	}

	@Test
	public void testbuildUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, null, null, null, null, null);

		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de"), "http://www.heise.de").toExternalForm());
		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "http://www.heise.de/").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/home.html"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/app/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/app/home.html"), "index.html").toExternalForm());
	}

	@Test
	public void testCleanUpUrl() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, null, null, null, null, null);

		assertEquals("http://www.heise.de", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de"));
		assertEquals("http://www.heise.de", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/"));
		assertEquals("http://www.heise.de", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de#bla"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de//index.html"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de//index.html#foo"));
		assertEquals("http://www.heise.de/index.html?a=b", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de//index.html?a=b#foo"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/foo/../index.html"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/../index.html"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/foo/../foo/../index.html"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/foo/../index.html#bla"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/../index.html#bla"));
		assertEquals("http://www.heise.de/index.html", websearchCrawlerNotify.cleanUpUrl("http://www.heise.de/foo/../foo/../index.html#bla"));
	}

	@Test
	public void testParseLinks() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final String content = "foo";
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.parseLinks(content)).andReturn(Arrays.asList("links", "javascript:alert();", " javascript:alert(); "));
		EasyMock.replay(htmlUtil);

		final CrawlerResult crawlerResult = EasyMock.createMock(CrawlerResult.class);
		EasyMock.expect(crawlerResult.getContent()).andReturn(content);
		EasyMock.expect(crawlerResult.getUrl()).andReturn(new URL("http://example.com"));
		EasyMock.replay(crawlerResult);

		final WebsearchPageDao pageDao = EasyMock.createMock(WebsearchPageDao.class);
		EasyMock.expect(pageDao.findOrCreate(new URL("http://example.com/links"))).andReturn(null);
		EasyMock.replay(pageDao);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, null, null, null, pageDao, htmlUtil);
		websearchCrawlerNotify.parseLinks(crawlerResult);

		EasyMock.verify(logger, htmlUtil, crawlerResult, pageDao);
	}

	@Test
	public void testIsIndexAble() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, null, null, null, null, null);

		assertTrue(websearchCrawlerNotify.isIndexAble(createCrawlerResult(true, "text/html")));
		assertTrue(websearchCrawlerNotify.isIndexAble(createCrawlerResult(true, "text/html;charset=UTF-8")));
		assertFalse(websearchCrawlerNotify.isIndexAble(createCrawlerResult(true, "text/plain")));
		assertFalse(websearchCrawlerNotify.isIndexAble(createCrawlerResult(false, "text/html")));
		assertFalse(websearchCrawlerNotify.isIndexAble(createCrawlerResult(false, "text/plain")));
		assertFalse(websearchCrawlerNotify.isIndexAble(createCrawlerResult(true, null)));
	}

	protected CrawlerResult createCrawlerResult(final boolean avaible, final String contentType) {
		return new CrawlerResult() {

			@Override
			public boolean isAvailable() {
				return avaible;
			}

			@Override
			public URL getUrl() {
				return null;
			}

			@Override
			public String getContentType() {
				return contentType;
			}

			@Override
			public String getContent() {
				return null;
			}
		};
	}
}

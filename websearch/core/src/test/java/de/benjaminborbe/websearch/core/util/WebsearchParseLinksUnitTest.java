package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.crawler.api.CrawlerResult;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class WebsearchParseLinksUnitTest {

	@Test
	public void testParseLinks() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final String content = "foo";
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.parseLinks(content)).andReturn(Arrays.asList("links", "javascript:alert();", " javascript:alert(); "));
		EasyMock.replay(htmlUtil);

		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		EasyMock.expect(httpContent.getContent()).andReturn(content.getBytes());
		EasyMock.replay(httpContent);

		final CrawlerResult crawlerResult = EasyMock.createMock(CrawlerResult.class);
		EasyMock.expect(crawlerResult.getContent()).andReturn(httpContent);
		EasyMock.expect(crawlerResult.getUrl()).andReturn(new URL("http://example.com"));
		EasyMock.replay(crawlerResult);

		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		final String urlString = "http://example.com/links";
		final URL url = new URL(urlString);
		EasyMock.expect(websearchPageDao.findOrCreate(url)).andReturn(null);
		EasyMock.replay(websearchPageDao);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseURL(urlString)).andReturn(url).anyTimes();
		EasyMock.replay(parseUtil);

		final HttpUtil httpUtil = new HttpUtil();

		final WebsearchPageBean websearchPageBean = EasyMock.createMock(WebsearchPageBean.class);
		EasyMock.replay(websearchPageBean);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil);
		websearchCrawlerNotify.parseLinks(crawlerResult);

		EasyMock.verify(logger, htmlUtil, crawlerResult, websearchPageDao, websearchPageDao);
	}

	@Test
	public void testCleanUpUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseURL("http://www.heise.de")).andReturn(new URL("http://www.heise.de")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/index.html")).andReturn(new URL("http://www.rocketnews.de/index.html")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/app/index.html")).andReturn(new URL("http://www.rocketnews.de/app/index.html")).anyTimes();
		EasyMock.replay(parseUtil);

		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		EasyMock.replay(websearchPageDao);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		EasyMock.replay(httpUtil);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil);

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
	public void testbuildUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseURL("http://www.heise.de")).andReturn(new URL("http://www.heise.de")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/index.html")).andReturn(new URL("http://www.rocketnews.de/index.html")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/app/index.html")).andReturn(new URL("http://www.rocketnews.de/app/index.html")).anyTimes();
		EasyMock.replay(parseUtil);

		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		EasyMock.replay(websearchPageDao);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		EasyMock.replay(httpUtil);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil);

		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de"), "http://www.heise.de").toExternalForm());
		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "http://www.heise.de/").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/home.html"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/app/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/app/home.html"), "index.html").toExternalForm());
	}
}

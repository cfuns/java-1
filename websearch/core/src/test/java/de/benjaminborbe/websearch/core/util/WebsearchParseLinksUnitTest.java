package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.MathUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

public class WebsearchParseLinksUnitTest {

	@Test
	public void testParseLinks() throws Exception {

		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		final Long depth = 0l;
		final Integer timeout = 5000;
		final String content = "foo";
		final String urlString = "http://example.com/links";
		final URL url = new URL(urlString);
		final String encoding = "utf8";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		final CrawlerNotifierResult crawlerResult = EasyMock.createMock(CrawlerNotifierResult.class);
		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		final WebsearchPageBean websearchPageBean = EasyMock.createMock(WebsearchPageBean.class);
		final MathUtil mathUtil = EasyMock.createMock(MathUtil.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);

		EasyMock.expect(htmlUtil.parseLinks(content)).andReturn(Arrays.asList("links", "javascript:alert();", " javascript:alert(); "));
		EasyMock.expect(httpContent.getContent()).andReturn(content.getBytes());
		EasyMock.expect(crawlerResult.getContent()).andReturn(httpContent);
		EasyMock.expect(crawlerResult.getUrl()).andReturn(new URL("http://example.com"));
		EasyMock.expect(crawlerResult.getHeader()).andReturn(null);
		EasyMock.expect(crawlerResult.getDepth()).andReturn(depth);
		EasyMock.expect(crawlerResult.getTimeout()).andReturn(timeout);
		EasyMock.expect(websearchPageDao.findOrCreate(url)).andReturn(websearchPageBean);
		EasyMock.expect(parseUtil.parseURL(urlString)).andReturn(url).anyTimes();
		EasyMock.expect(websearchPageBean.getDepth()).andReturn(depth);
		EasyMock.expect(mathUtil.maxLong(depth, depth)).andReturn(depth);
		websearchPageBean.setDepth(depth);
		EasyMock.expect(websearchPageDao.getEncoding()).andReturn(encoding);
		websearchPageDao.save(EasyMock.anyObject(WebsearchPageBean.class), EasyMock.anyObject(Collection.class));

		EasyMock.expect(websearchPageBean.getTimeout()).andReturn(timeout);
		EasyMock.expect(mathUtil.maxInteger(timeout, timeout)).andReturn(timeout);
		websearchPageBean.setTimeout(timeout);

		final Object[] objects = {logger, htmlUtil, crawlerResult, websearchPageDao, mathUtil, websearchPageBean, parseUtil, httpContent};
		EasyMock.replay(objects);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil, mathUtil);
		websearchCrawlerNotify.parseLinks(crawlerResult);

		EasyMock.verify(objects);
	}

	@Test
	public void testCleanUpUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseURL("http://www.heise.de")).andReturn(new URL("http://www.heise.de")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/index.html")).andReturn(new URL("http://www.rocketnews.de/index.html")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/app/index.html")).andReturn(new URL("http://www.rocketnews.de/app/index.html")).anyTimes();
		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final MathUtil mathUtil = EasyMock.createMock(MathUtil.class);

		final Object[] objects = {logger, htmlUtil, websearchPageDao, mathUtil, parseUtil, httpUtil};
		EasyMock.replay(objects);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil, mathUtil);

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

		EasyMock.verify(objects);
	}

	@Test
	public void testbuildUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseURL("http://www.heise.de")).andReturn(new URL("http://www.heise.de")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/index.html")).andReturn(new URL("http://www.rocketnews.de/index.html")).anyTimes();
		EasyMock.expect(parseUtil.parseURL("http://www.rocketnews.de/app/index.html")).andReturn(new URL("http://www.rocketnews.de/app/index.html")).anyTimes();
		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final MathUtil mathUtil = EasyMock.createMock(MathUtil.class);

		final Object[] objects = {logger, htmlUtil, websearchPageDao, mathUtil, parseUtil, httpUtil};
		EasyMock.replay(objects);

		final WebsearchParseLinks websearchCrawlerNotify = new WebsearchParseLinks(logger, htmlUtil, httpUtil, websearchPageDao, parseUtil, mathUtil);

		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de"), "http://www.heise.de").toExternalForm());
		assertEquals("http://www.heise.de", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "http://www.heise.de/").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/home.html"), "index.html").toExternalForm());
		assertEquals("http://www.rocketnews.de/app/index.html", websearchCrawlerNotify.buildUrl(new URL("http://www.rocketnews.de/app/home.html"), "index.html").toExternalForm());

		EasyMock.verify(objects);
	}
}

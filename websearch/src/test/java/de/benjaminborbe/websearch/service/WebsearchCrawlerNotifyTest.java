package de.benjaminborbe.websearch.service;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.page.PageDao;

public class WebsearchCrawlerNotifyTest {

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

		final PageDao pageDao = EasyMock.createMock(PageDao.class);
		EasyMock.replay(pageDao);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, indexerService, stringUtil, pageDao);
		assertEquals(title, websearchCrawlerNotify.extractTitle(content));
	}
}

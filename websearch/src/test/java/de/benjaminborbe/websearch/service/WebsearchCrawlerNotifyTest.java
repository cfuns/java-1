package de.benjaminborbe.websearch.service;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.index.api.IndexerService;

public class WebsearchCrawlerNotifyTest {

	@Test
	public void testExtractTitle() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final IndexerService indexerService = EasyMock.createMock(IndexerService.class);
		EasyMock.replay(indexerService);
		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, indexerService);
		final String title = "Foo Bar Title";
		final String content = "<html><title>" + title + "</title><body></body></html>";
		assertEquals(title, websearchCrawlerNotify.extractTitle(content));
	}
}

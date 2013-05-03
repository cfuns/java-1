package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import de.benjaminborbe.httpdownloader.api.HttpResponseDto;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class CrawlerNotifierUnitTest {

	@Test
	public void testNotify() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpResponseDto result = new HttpResponseDto();

		final CrawlerNotifier clientCrawlerNotifier = EasyMock.createMock(CrawlerNotifier.class);
		clientCrawlerNotifier.notifiy(result);
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(clientCrawlerNotifier);

		final Set<CrawlerNotifier> crawlerNotifiers = new HashSet<>();
		crawlerNotifiers.add(clientCrawlerNotifier);

		final CrawlerNotifierRegistry crawlerNotifierRegistry = EasyMock.createMock(CrawlerNotifierRegistry.class);
		EasyMock.expect(crawlerNotifierRegistry.getAll()).andReturn(crawlerNotifiers).times(1);
		EasyMock.replay(crawlerNotifierRegistry);

		final CrawlerNotifier crawlerNotifier = new CrawlerNotifierImpl(logger, crawlerNotifierRegistry);
		crawlerNotifier.notifiy(result);

		// notify result should be called 1 time
		EasyMock.verify(clientCrawlerNotifier);
	}
}

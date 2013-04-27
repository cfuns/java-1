package de.benjaminborbe.crawler.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.message.CrawlerMessageMapper;
import de.benjaminborbe.crawler.message.CrawlerMessageMapperImpl;
import de.benjaminborbe.crawler.service.CrawlerNotifierImpl;
import de.benjaminborbe.crawler.service.CrawlerServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class CrawlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CrawlerMessageMapper.class).to(CrawlerMessageMapperImpl.class).in(Singleton.class);
		bind(CrawlerService.class).to(CrawlerServiceImpl.class).in(Singleton.class);
		bind(CrawlerNotifier.class).to(CrawlerNotifierImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

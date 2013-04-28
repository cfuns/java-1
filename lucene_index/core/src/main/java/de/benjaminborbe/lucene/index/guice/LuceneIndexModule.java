package de.benjaminborbe.lucene.index.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfig;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfigImpl;
import de.benjaminborbe.lucene.index.service.LuceneIndexServiceImpl;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactory;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactoryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class LuceneIndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(LuceneIndexService.class).to(LuceneIndexServiceImpl.class).in(Singleton.class);
		bind(LuceneIndexConfig.class).to(LuceneIndexConfigImpl.class).in(Singleton.class);
		bind(LuceneIndexFactory.class).to(LuceneIndexFactoryImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

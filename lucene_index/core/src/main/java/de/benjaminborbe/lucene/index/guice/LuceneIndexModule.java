package de.benjaminborbe.lucene.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfig;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfigImpl;
import de.benjaminborbe.lucene.index.service.LuceneIndexServiceImpl;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactory;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactoryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class LuceneIndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LuceneIndexService.class).to(LuceneIndexServiceImpl.class).in(Singleton.class);
		bind(LuceneIndexConfig.class).to(LuceneIndexConfigImpl.class).in(Singleton.class);
		bind(LuceneIndexFactory.class).to(LuceneIndexFactoryImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

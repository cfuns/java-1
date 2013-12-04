package de.benjaminborbe.search.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.core.config.SearchConfig;
import de.benjaminborbe.search.core.config.SearchConfigImpl;
import de.benjaminborbe.search.core.dao.SearchQueryHistoryDao;
import de.benjaminborbe.search.core.dao.SearchQueryHistoryDaoStorage;
import de.benjaminborbe.search.core.service.SearchServiceImpl;
import de.benjaminborbe.search.core.util.SearchServiceComponentRegistry;
import de.benjaminborbe.search.core.util.SearchServiceComponentRegistryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(SearchConfig.class).to(SearchConfigImpl.class).in(Singleton.class);
		bind(SearchQueryHistoryDao.class).to(SearchQueryHistoryDaoStorage.class).in(Singleton.class);
		bind(SearchServiceComponentRegistry.class).to(SearchServiceComponentRegistryImpl.class).in(Singleton.class);
		bind(SearchService.class).to(SearchServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

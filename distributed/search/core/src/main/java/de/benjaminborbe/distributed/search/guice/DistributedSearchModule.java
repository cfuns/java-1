package de.benjaminborbe.distributed.search.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageDao;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageDaoStorage;
import de.benjaminborbe.distributed.search.service.DistributedSearchServiceImpl;
import de.benjaminborbe.distributed.search.util.StopWords;
import de.benjaminborbe.distributed.search.util.StopWordsProvider;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class DistributedSearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DistributedSearchPageDao.class).to(DistributedSearchPageDaoStorage.class).in(Singleton.class);
		bind(DistributedSearchService.class).to(DistributedSearchServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(StopWords.class).toProvider(StopWordsProvider.class).in(Singleton.class);

		requestStaticInjection(DistributedSearchValidatorLinker.class);
	}
}

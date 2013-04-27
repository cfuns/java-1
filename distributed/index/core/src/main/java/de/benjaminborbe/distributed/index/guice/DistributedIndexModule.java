package de.benjaminborbe.distributed.index.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryDaoStorage;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordDaoStorage;
import de.benjaminborbe.distributed.index.service.DistributedIndexServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class DistributedIndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DistributedIndexWordDao.class).to(DistributedIndexWordDaoStorage.class).in(Singleton.class);
		bind(DistributedIndexEntryDao.class).to(DistributedIndexEntryDaoStorage.class).in(Singleton.class);
		bind(DistributedIndexService.class).to(DistributedIndexServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

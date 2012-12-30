package de.benjaminborbe.distributed.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.dao.DistributedIndexDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexDaoStorage;
import de.benjaminborbe.distributed.index.service.DistributedIndexServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class DistributedIndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DistributedIndexDao.class).to(DistributedIndexDaoStorage.class).in(Singleton.class);
		bind(DistributedIndexService.class).to(DistributedIndexServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

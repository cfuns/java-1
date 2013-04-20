package de.benjaminborbe.dashboard.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.dao.DashboardDao;
import de.benjaminborbe.dashboard.dao.DashboardDaoStorage;
import de.benjaminborbe.dashboard.service.DashboardServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class DashboardModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DashboardDao.class).to(DashboardDaoStorage.class).in(Singleton.class);
		bind(DashboardService.class).to(DashboardServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

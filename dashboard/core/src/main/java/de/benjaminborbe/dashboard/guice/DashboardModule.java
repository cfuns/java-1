package de.benjaminborbe.dashboard.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.dao.DashboardDao;
import de.benjaminborbe.dashboard.dao.DashboardDaoStorage;
import de.benjaminborbe.dashboard.service.DashboardServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class DashboardModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DashboardDao.class).to(DashboardDaoStorage.class).in(Singleton.class);
		bind(DashboardService.class).to(DashboardServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

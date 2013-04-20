package de.benjaminborbe.monitoring.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.config.MonitoringConfigImpl;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDaoStorage;
import de.benjaminborbe.monitoring.service.MonitoringServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MonitoringNodeDao.class).to(MonitoringNodeDaoStorage.class).in(Singleton.class);
		bind(MonitoringConfig.class).to(MonitoringConfigImpl.class).in(Singleton.class);
		bind(MonitoringService.class).to(MonitoringServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(MonitoringValidatorLinker.class);
	}
}

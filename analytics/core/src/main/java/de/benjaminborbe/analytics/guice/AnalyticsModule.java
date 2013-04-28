package de.benjaminborbe.analytics.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.config.AnalyticsConfig;
import de.benjaminborbe.analytics.config.AnalyticsConfigImpl;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportDaoStorage;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDaoStorage;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDaoStorage;
import de.benjaminborbe.analytics.service.AnalyticsServiceImpl;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class AnalyticsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(AnalyticsConfig.class).to(AnalyticsConfigImpl.class).in(Singleton.class);
		bind(AnalyticsReportLogDao.class).to(AnalyticsReportLogDaoStorage.class).in(Singleton.class);
		bind(AnalyticsReportValueDao.class).to(AnalyticsReportValueDaoStorage.class).in(Singleton.class);
		bind(AnalyticsReportDao.class).to(AnalyticsReportDaoStorage.class).in(Singleton.class);
		bind(AnalyticsService.class).to(AnalyticsServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(AnalyticsValidatorLinker.class);
	}
}

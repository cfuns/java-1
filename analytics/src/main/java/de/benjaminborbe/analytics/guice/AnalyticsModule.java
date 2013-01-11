package de.benjaminborbe.analytics.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportDaoStorage;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDaoStorage;
import de.benjaminborbe.analytics.service.AnalyticsServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AnalyticsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AnalyticsReportValueDao.class).to(AnalyticsReportValueDaoStorage.class).in(Singleton.class);
		bind(AnalyticsReportDao.class).to(AnalyticsReportDaoStorage.class).in(Singleton.class);
		bind(AnalyticsService.class).to(AnalyticsServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(AnalyticsValidatorLinker.class);
	}
}

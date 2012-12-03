package de.benjaminborbe.confluence.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorImpl;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDaoStorage;
import de.benjaminborbe.confluence.dao.ConfluencePageDao;
import de.benjaminborbe.confluence.dao.ConfluencePageDaoStorage;
import de.benjaminborbe.confluence.service.ConfluenceServiceImpl;
import de.benjaminborbe.confluence.validation.ConfluenceInstanceValidatorLinker;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ConfluenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfluencePageDao.class).to(ConfluencePageDaoStorage.class).in(Singleton.class);
		bind(ConfluenceInstanceDao.class).to(ConfluenceInstanceDaoStorage.class).in(Singleton.class);
		bind(ConfluenceConnector.class).to(ConfluenceConnectorImpl.class).in(Singleton.class);
		bind(ConfluenceService.class).to(ConfluenceServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(ConfluenceInstanceValidatorLinker.class);
	}
}

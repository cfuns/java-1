package de.benjaminborbe.checklist.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.dao.ChecklistEntryDao;
import de.benjaminborbe.checklist.dao.ChecklistEntryDaoStorage;
import de.benjaminborbe.checklist.dao.ChecklistListDao;
import de.benjaminborbe.checklist.dao.ChecklistListDaoStorage;
import de.benjaminborbe.checklist.service.ChecklistServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ChecklistModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ChecklistEntryDao.class).to(ChecklistEntryDaoStorage.class).in(Singleton.class);
		bind(ChecklistListDao.class).to(ChecklistListDaoStorage.class).in(Singleton.class);
		bind(ChecklistService.class).to(ChecklistServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(ChecklistValidatorLinker.class);
	}
}

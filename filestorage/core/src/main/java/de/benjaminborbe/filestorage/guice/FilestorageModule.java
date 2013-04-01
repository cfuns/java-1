package de.benjaminborbe.filestorage.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.dao.FilestorageEntryDao;
import de.benjaminborbe.filestorage.dao.FilestorageEntryDaoStorage;
import de.benjaminborbe.filestorage.service.FilestorageServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class FilestorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FilestorageEntryDao.class).to(FilestorageEntryDaoStorage.class).in(Singleton.class);
		bind(FilestorageService.class).to(FilestorageServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(FilestorageValidatorLinker.class);
	}
}

package de.benjaminborbe.note.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.dao.NoteDao;
import de.benjaminborbe.note.dao.NoteDaoStorage;
import de.benjaminborbe.note.service.NoteServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class NoteModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NoteDao.class).to(NoteDaoStorage.class).in(Singleton.class);
		bind(NoteService.class).to(NoteServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

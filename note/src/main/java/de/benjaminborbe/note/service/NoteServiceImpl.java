package de.benjaminborbe.note.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.note.api.NoteService;

@Singleton
public class NoteServiceImpl implements NoteService {

	private final Logger logger;

	@Inject
	public NoteServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}

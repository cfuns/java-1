package de.benjaminborbe.checklist.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.checklist.api.ChecklistService;

@Singleton
public class ChecklistServiceImpl implements ChecklistService {

	private final Logger logger;

	@Inject
	public ChecklistServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}

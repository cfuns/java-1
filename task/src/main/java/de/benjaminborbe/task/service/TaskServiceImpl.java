package de.benjaminborbe.task.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.task.api.TaskService;

@Singleton
public class TaskServiceImpl implements TaskService {

	private final Logger logger;

	@Inject
	public TaskServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}

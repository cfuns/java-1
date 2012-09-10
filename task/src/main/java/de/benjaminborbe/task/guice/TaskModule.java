package de.benjaminborbe.task.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.service.TaskServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class TaskModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TaskService.class).to(TaskServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

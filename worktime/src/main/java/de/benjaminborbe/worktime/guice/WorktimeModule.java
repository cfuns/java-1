package de.benjaminborbe.worktime.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.worktime.api.WorktimeRecorder;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.service.WorktimeServiceImpl;
import de.benjaminborbe.worktime.util.WorktimeRecorderImpl;

public class WorktimeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WorktimeRecorder.class).to(WorktimeRecorderImpl.class).in(Singleton.class);
		bind(WorktimeService.class).to(WorktimeServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

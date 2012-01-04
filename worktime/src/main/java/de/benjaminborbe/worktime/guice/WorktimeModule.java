package de.benjaminborbe.worktime.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRendererImpl;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.worktime.api.WorktimeRecorder;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.service.WorktimeServiceImpl;
import de.benjaminborbe.worktime.util.WorktimeRecorderImpl;
import de.benjaminborbe.worktime.util.WorktimeStorageService;
import de.benjaminborbe.worktime.util.WorktimeStorageServiceImpl;

public class WorktimeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JavascriptResourceRenderer.class).to(JavascriptResourceRendererImpl.class).in(Singleton.class);
		bind(ParseUtil.class).to(ParseUtilImpl.class).in(Singleton.class);
		bind(TimeZoneUtil.class).to(TimeZoneUtilImpl.class).in(Singleton.class);
		bind(CalendarUtil.class).to(CalendarUtilImpl.class).in(Singleton.class);
		bind(WorktimeStorageService.class).to(WorktimeStorageServiceImpl.class).in(Singleton.class);
		bind(WorktimeRecorder.class).to(WorktimeRecorderImpl.class).in(Singleton.class);
		bind(WorktimeService.class).to(WorktimeServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

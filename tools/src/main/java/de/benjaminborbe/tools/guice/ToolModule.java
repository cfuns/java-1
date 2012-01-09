package de.benjaminborbe.tools.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.DateUtilImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class ToolModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TimeZoneUtil.class).to(TimeZoneUtilImpl.class).in(Singleton.class);
		bind(ParseUtil.class).to(ParseUtilImpl.class).in(Singleton.class);
		bind(DateUtil.class).to(DateUtilImpl.class).in(Singleton.class);
		bind(CalendarUtil.class).to(CalendarUtilImpl.class).in(Singleton.class);
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
		bind(HttpDownloader.class).to(HttpDownloaderImpl.class).in(Singleton.class);
		bind(IdGenerator.class).to(IdGeneratorImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

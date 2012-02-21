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
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderImpl;
import de.benjaminborbe.tools.jndi.JndiContext;
import de.benjaminborbe.tools.jndi.JndiContextImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.IdGeneratorLong;
import de.benjaminborbe.tools.util.IdGeneratorLongImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.tools.util.StringUtilImpl;
import de.benjaminborbe.tools.util.ThreadPoolExecuter;
import de.benjaminborbe.tools.util.ThreadPoolExecuterImpl;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class ToolModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ThreadPoolExecuter.class).to(ThreadPoolExecuterImpl.class);
		bind(UrlUtil.class).to(UrlUtilImpl.class).in(Singleton.class);
		bind(HtmlUtil.class).to(HtmlUtilImpl.class).in(Singleton.class);
		bind(ResourceUtil.class).to(ResourceUtilImpl.class).in(Singleton.class);
		bind(StringUtil.class).to(StringUtilImpl.class).in(Singleton.class);
		bind(JndiContext.class).to(JndiContextImpl.class).in(Singleton.class);
		bind(Base64Util.class).to(Base64UtilImpl.class).in(Singleton.class);
		bind(TimeZoneUtil.class).to(TimeZoneUtilImpl.class).in(Singleton.class);
		bind(ParseUtil.class).to(ParseUtilImpl.class).in(Singleton.class);
		bind(DateUtil.class).to(DateUtilImpl.class).in(Singleton.class);
		bind(CalendarUtil.class).to(CalendarUtilImpl.class).in(Singleton.class);
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
		bind(HttpDownloader.class).to(HttpDownloaderImpl.class).in(Singleton.class);
		bind(IdGeneratorLong.class).to(IdGeneratorLongImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

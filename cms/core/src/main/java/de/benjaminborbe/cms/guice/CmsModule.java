package de.benjaminborbe.cms.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.cms.api.CmsService;
import de.benjaminborbe.cms.service.CmsServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class CmsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CmsService.class).to(CmsServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

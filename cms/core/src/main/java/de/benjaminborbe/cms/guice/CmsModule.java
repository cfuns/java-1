package de.benjaminborbe.cms.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.cms.api.CmsService;
import de.benjaminborbe.cms.service.CmsServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class CmsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CmsService.class).to(CmsServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

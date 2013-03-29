package de.benjaminborbe.virt.core.guice;

import de.benjaminborbe.virt.core.service.VirtCoreServiceImpl;
import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class VirtCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VirtService.class).to(VirtCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

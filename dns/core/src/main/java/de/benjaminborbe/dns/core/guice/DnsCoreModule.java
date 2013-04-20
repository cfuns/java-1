package de.benjaminborbe.dns.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.core.service.DnsCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class DnsCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DnsService.class).to(DnsCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}

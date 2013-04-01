package de.benjaminborbe.authentication.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.storage.api.StorageService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class AuthenticationOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ShortenerService.class).toProvider(service(ShortenerService.class).single());
		bind(MailService.class).toProvider(service(MailService.class).single());
		bind(ConfigurationService.class).toProvider(service(ConfigurationService.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

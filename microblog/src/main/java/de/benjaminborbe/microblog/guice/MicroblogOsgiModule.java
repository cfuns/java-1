package de.benjaminborbe.microblog.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;

public class MicroblogOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(MailService.class).toProvider(service(MailService.class).single());
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

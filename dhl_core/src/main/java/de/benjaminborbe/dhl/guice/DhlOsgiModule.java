package de.benjaminborbe.dhl.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.storage.api.StorageService;

public class DhlOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthorizationService.class).toProvider(service(AuthorizationService.class).single());
		bind(AuthenticationService.class).toProvider(service(AuthenticationService.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(NotificationService.class).toProvider(service(NotificationService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

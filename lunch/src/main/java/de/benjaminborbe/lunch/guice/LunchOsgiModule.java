package de.benjaminborbe.lunch.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.xmpp.api.XmppService;

public class LunchOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NotificationService.class).toProvider(service(NotificationService.class).single());
		bind(XmppService.class).toProvider(service(XmppService.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(KioskService.class).toProvider(service(KioskService.class).single());
		bind(MailService.class).toProvider(service(MailService.class).single());
		bind(AuthenticationService.class).toProvider(service(AuthenticationService.class).single());
		bind(AuthorizationService.class).toProvider(service(AuthorizationService.class).single());
		bind(ConfigurationService.class).toProvider(service(ConfigurationService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

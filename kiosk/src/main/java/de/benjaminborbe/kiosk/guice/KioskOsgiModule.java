package de.benjaminborbe.kiosk.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.message.api.MessageService;

public class KioskOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MessageService.class).toProvider(service(MessageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

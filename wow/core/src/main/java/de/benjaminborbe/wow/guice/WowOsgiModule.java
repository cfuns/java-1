package de.benjaminborbe.wow.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.vnc.api.VncService;

public class WowOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VncService.class).toProvider(service(VncService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

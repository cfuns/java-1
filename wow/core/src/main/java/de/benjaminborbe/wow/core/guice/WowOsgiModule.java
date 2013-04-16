package de.benjaminborbe.wow.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.vnc.api.VncService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class WowOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VncService.class).toProvider(service(VncService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

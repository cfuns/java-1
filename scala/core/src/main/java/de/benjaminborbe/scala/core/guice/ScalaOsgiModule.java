package de.benjaminborbe.scala.core.guice;

import com.google.inject.AbstractModule;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class ScalaOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}

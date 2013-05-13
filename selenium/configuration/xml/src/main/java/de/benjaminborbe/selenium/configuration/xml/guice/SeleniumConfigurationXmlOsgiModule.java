package de.benjaminborbe.selenium.configuration.xml.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.storage.api.StorageService;

import static org.ops4j.peaberry.Peaberry.service;

public class SeleniumConfigurationXmlOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(SeleniumService.class).toProvider(service(SeleniumService.class).single());
	}
}

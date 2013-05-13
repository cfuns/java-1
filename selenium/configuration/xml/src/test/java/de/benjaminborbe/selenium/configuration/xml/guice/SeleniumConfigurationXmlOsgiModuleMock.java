package de.benjaminborbe.selenium.configuration.xml.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.mock.SeleniumServiceMock;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;

import javax.inject.Singleton;

public class SeleniumConfigurationXmlOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).to(StorageServiceMock.class).in(Singleton.class);
		bind(SeleniumService.class).to(SeleniumServiceMock.class).in(Singleton.class);
	}
}

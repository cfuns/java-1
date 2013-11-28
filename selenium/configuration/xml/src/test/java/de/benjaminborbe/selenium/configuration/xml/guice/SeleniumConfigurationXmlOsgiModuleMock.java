package de.benjaminborbe.selenium.configuration.xml.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.mock.SeleniumServiceMock;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.memory.service.StorageServiceMemory;

import javax.inject.Singleton;

public class SeleniumConfigurationXmlOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).to(StorageServiceMemory.class).in(Singleton.class);
		bind(SeleniumService.class).to(SeleniumServiceMock.class).in(Singleton.class);
	}
}

package de.benjaminborbe.configuration.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigurationServiceMock implements ConfigurationService {

	@Inject
	public ConfigurationServiceMock() {
	}

	@Override
	public void execute() {
	}
}

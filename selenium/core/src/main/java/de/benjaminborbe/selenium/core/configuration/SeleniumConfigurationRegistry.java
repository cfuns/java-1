package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.tools.registry.Registry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SeleniumConfigurationRegistry implements Registry<SeleniumConfiguration> {

	private final Map<SeleniumConfigurationIdentifier, SeleniumConfiguration> data = new HashMap<>();

	@Inject
	public SeleniumConfigurationRegistry() {
	}

	public SeleniumConfiguration get(final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier) {
		return data.get(seleniumConfigurationIdentifier);
	}

	@Override
	public void add(final SeleniumConfiguration object) {
		data.put(object.getId(), object);
	}

	@Override
	public void add(final SeleniumConfiguration... objects) {
		for (SeleniumConfiguration object : objects) {
			add(object);
		}
	}

	@Override
	public void remove(final SeleniumConfiguration object) {
		data.remove(object.getId());
	}

	@Override
	public Collection<SeleniumConfiguration> getAll() {
		return data.values();
	}
}

package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.tools.registry.Registry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SeleniumConfigurationRegistry implements Registry<SeleniumConfigurationAction> {

	private final Map<SeleniumConfigurationIdentifier, SeleniumConfigurationAction> data = new HashMap<>();

	@Inject
	public SeleniumConfigurationRegistry(final SeleniumConfigurationSimple seleniumConfigurationSimple) {
		add(seleniumConfigurationSimple);
	}

	public SeleniumConfigurationAction get(final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier) {
		return data.get(seleniumConfigurationIdentifier);
	}

	@Override
	public void add(final SeleniumConfigurationAction object) {
		data.put(object.getId(), object);
	}

	@Override
	public void add(final SeleniumConfigurationAction... objects) {
		for (SeleniumConfigurationAction object : objects) {
			add(object);
		}
	}

	@Override
	public void remove(final SeleniumConfigurationAction object) {
		data.remove(object.getId());
	}

	@Override
	public Collection<SeleniumConfigurationAction> getAll() {
		return data.values();
	}
}

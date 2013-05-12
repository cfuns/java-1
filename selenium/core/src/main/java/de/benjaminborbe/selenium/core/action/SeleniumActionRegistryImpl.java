package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.tools.registry.Registry;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SeleniumActionRegistryImpl implements SeleniumActionRegistry, Registry<SeleniumAction> {

	private final Map<Class<SeleniumActionConfiguration>, SeleniumAction> data = new HashMap<>();

	@Inject
	public SeleniumActionRegistryImpl(
		final SeleniumActionPageInfo seleniumActionPageInfo,
		final SeleniumActionGetUrl seleniumActionGetUrl,
		final SeleniumActionPageContent seleniumActionPageContent,
		final SeleniumActionClick seleniumActionClick,
		SeleniumActionExpectText seleniumActionExpectText
	) {
		add(seleniumActionPageInfo);
		add(seleniumActionGetUrl);
		add(seleniumActionPageContent);
		add(seleniumActionClick);
		add(seleniumActionExpectText);
	}

	@Override
	public SeleniumAction<SeleniumActionConfiguration> get(final Class<? extends SeleniumActionConfiguration> seleniumActionConfigurationClass) {
		return data.get(seleniumActionConfigurationClass);
	}

	@Override
	public void add(final SeleniumAction object) {
		final Class type = object.getType();
		if (data.put(type, object) != null) {
			throw new SeleniumActionRegistryAlreadyRegisteredException("action for type " + type + " already registered");
		}
	}

	@Override
	public void add(final SeleniumAction... objects) {
		for (final SeleniumAction object : objects) {
			add(object);
		}
	}

	@Override
	public void remove(final SeleniumAction object) {
		data.remove(object.getType().getClass());
	}

	@Override
	public Collection<SeleniumAction> getAll() {
		return data.values();
	}
}

package de.benjaminborbe.selenium.api;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;

import java.util.List;

public class SeleniumConfigurationDto implements SeleniumConfiguration {

	private List<SeleniumActionConfiguration> actions;

	private String name;

	private SeleniumConfigurationIdentifier id;

	public void setActions(final List<SeleniumActionConfiguration> actions) {
		this.actions = actions;
	}

	public void setId(final SeleniumConfigurationIdentifier id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public SeleniumConfigurationIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<SeleniumActionConfiguration> getActionConfigurations() {
		return actions;
	}
}

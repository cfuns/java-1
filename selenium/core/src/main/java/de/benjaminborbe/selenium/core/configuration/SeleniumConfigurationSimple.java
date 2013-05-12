package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import javax.inject.Inject;

public class SeleniumConfigurationSimple implements SeleniumConfiguration {

	@Inject
	public SeleniumConfigurationSimple() {
	}

	@Override
	public SeleniumConfigurationIdentifier getId() {
		return new SeleniumConfigurationIdentifier("1");
	}

	@Override
	public String getName() {
		return "simple";
	}
}

package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public class SeleniumConfigurationIdentifierBuilder implements IdentifierBuilder<String, SeleniumConfigurationIdentifier> {

	@Override
	public SeleniumConfigurationIdentifier buildIdentifier(final String value) {
		return new SeleniumConfigurationIdentifier(value);
	}

}

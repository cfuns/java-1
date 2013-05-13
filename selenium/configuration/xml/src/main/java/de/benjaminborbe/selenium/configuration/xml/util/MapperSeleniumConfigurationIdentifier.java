package de.benjaminborbe.selenium.configuration.xml.util;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperSeleniumConfigurationIdentifier implements Mapper<SeleniumConfigurationIdentifier> {

	public String toString(final SeleniumConfigurationIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public SeleniumConfigurationIdentifier fromString(final String value) {
		return value != null ? new SeleniumConfigurationIdentifier(value) : null;
	}

}

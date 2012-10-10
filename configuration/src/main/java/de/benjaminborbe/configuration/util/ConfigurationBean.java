package de.benjaminborbe.configuration.util;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class ConfigurationBean implements Entity<ConfigurationIdentifier> {

	private static final long serialVersionUID = 8032652320006340164L;

	private ConfigurationIdentifier id;

	private String value;

	@Override
	public ConfigurationIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ConfigurationIdentifier id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}

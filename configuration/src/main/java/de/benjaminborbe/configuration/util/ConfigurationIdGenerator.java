package de.benjaminborbe.configuration.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorLong;

@Singleton
public class ConfigurationIdGenerator implements IdGenerator<ConfigurationIdentifier> {

	private final IdGeneratorLong idGeneratorLong;

	@Inject
	public ConfigurationIdGenerator(final IdGeneratorLong idGeneratorLong) {
		this.idGeneratorLong = idGeneratorLong;
	}

	@Override
	public ConfigurationIdentifier nextId() {
		return new ConfigurationIdentifier(idGeneratorLong.nextId());
	}

}

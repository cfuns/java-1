package de.benjaminborbe.websearch.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorLong;

@Singleton
public class WebsearchConfigurationIdGenerator implements IdGenerator<WebsearchConfigurationIdentifier> {

	private final IdGeneratorLong idGeneratorLong;

	@Inject
	public WebsearchConfigurationIdGenerator(final IdGeneratorLong idGeneratorLong) {
		this.idGeneratorLong = idGeneratorLong;
	}

	@Override
	public WebsearchConfigurationIdentifier nextId() {
		return new WebsearchConfigurationIdentifier(idGeneratorLong.nextId());
	}

}

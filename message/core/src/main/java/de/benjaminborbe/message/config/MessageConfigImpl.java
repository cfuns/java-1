package de.benjaminborbe.message.config;

import com.google.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MessageConfigImpl extends ConfigurationBase implements MessageConfig {

	private final ConfigurationDescriptionInteger consumerAmount = new ConfigurationDescriptionInteger(10, "MessageConsumerAmount", "Message Consumer Amount");

	@Inject
	public MessageConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<>();
		result.add(consumerAmount);
		return result;
	}

	@Override
	public Integer getConsumerAmount() {
		return getValueInteger(consumerAmount);
	}

}
